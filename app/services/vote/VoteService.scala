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

  def add(vote: Vote) = {
    def createVoteIfPossible(question: Option[Question], answer: Option[Answer]) =
      (question, answer) match {
        case (Some(q), Some(a)) if a.isActive.value && q.isActive.value =>
          voteRepo.create(vote.validate).map(Option(_))
        case _ => Future.successful(None)
      }
    for {
      question <- questionService.findById(vote.questionId)
      answer <- answerService.findById(vote.answerId)
      voteId <- createVoteIfPossible(question, answer)
    } yield voteId
  }

  def findForUser(id: User.Id): Future[Map[Question.Id, List[Vote]]] = {
    for {
      forUser <- voteRepo.findForUser(id)
      grouped = forUser.groupBy(_.questionId)
      sorted = grouped.mapValues(_.sortBy(_.time))
    } yield sorted
  }

  def findForQuestionAndUser(questionId: Question.Id, userId: User.Id): Future[List[Vote]] = {
    for {
      forQuestionAndUser <- voteRepo.findForQuestionAndUser(questionId, userId)
      sorted = forQuestionAndUser.sortBy(_.time)
    } yield sorted
  }
}
