package services.vote

import domain.answer.Answer
import domain.exceptions.WrongQuestionTypeException
import domain.question.Question
import domain.user.User
import domain.vote.Vote
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.VoteRepo
import services.answer.AnswerService
import services.question.QuestionService
import utils.Joda._
import utils.Futures._

import scala.concurrent.Future

class VoteService(voteRepo: VoteRepo, questionService: QuestionService, answerService: AnswerService) {

  def add(vote: Vote): Future[Option[Vote.Id]] = {
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

  def addMultiple(votes: List[Vote]): Future[List[Vote.Id]] = {
    def validateQuestions(questions: List[Question]) = {
      if(questions.distinct.length == 1 && questions.forall(_.isMulti.value)) true
      else throw new WrongQuestionTypeException("Can not add multiple answers to single-answer question")
    }
    for {
      questions <- Future.flatTraverse(votes)(v => questionService.findById(v.questionId))
      _ = validateQuestions(questions)
      ids <- Future.flatTraverse(votes)(add)
    } yield ids
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
