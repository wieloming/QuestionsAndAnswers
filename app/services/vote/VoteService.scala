package services.vote

import domain.answer.Answer
import domain.question.Question
import domain.user.User
import domain.vote.Vote
import repositories.interfaces.VoteRepo
import services.answer.AnswerService
import services.question.QuestionService
import utils.Joda._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class VoteService(voteRepo: VoteRepo, questionService: QuestionService, answerService: AnswerService) {

  def add(questionId: Vote.id, vote: Vote) = {
    def createVoteIfValid(question: Option[Question], answer: Option[Answer]) = (question, answer) match {
      case (Some(q), Some(a)) if a.isActive.value && q.isActive.value =>
        voteRepo.create(vote).map(Option(_))
      case _ => Future.successful(None)
    }
    for {
      question <- questionService.findById(vote.questionId)
      answer <- answerService.findById(vote.answerId)
      voteId <- createVoteIfValid(question, answer)
    } yield voteId
  }

  def findForUser(id: User.id) = {
    for {
      forUser <- voteRepo.findForUser(id)
      grouped = forUser.groupBy(_.questionId)
      sorted = grouped.mapValues(_.sortBy(_.time))
    } yield sorted
  }

  def findForQuestionAndUser(questionId: Question.id, userId: User.id) = {
    for {
      forQuestionAndUser <- voteRepo.findForQuestionAndUser(questionId, userId)
      sorted = forQuestionAndUser.sortBy(_.time)
    } yield sorted
  }

}