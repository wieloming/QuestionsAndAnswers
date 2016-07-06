package services.question

import cats.implicits._
import domain.question.{Question, QuestionForCreateDto, QuestionForUpdateDto, QuestionWithAnswersDto}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.QuestionRepo
import services.answer.AnswerService
import utils.Futures._

import scala.concurrent.Future

class QuestionService(questionRepo: QuestionRepo, answerService: AnswerService) {

  def findById(questionId: Question.Id) = questionRepo.findById(questionId)

  def findWithAnswersById(id: Question.Id): Future[Option[QuestionWithAnswersDto]] = {
    for {
      question <- questionRepo.findById(id)
      answers <- answerService.findByQuestionId(id)
    } yield question.map(QuestionWithAnswersDto(_, answers))
  }

  def addQuestion(question: QuestionForCreateDto) = {
    for {
      id <- questionRepo.create(question.toQuestion)
      answers <- Future.traverse(question.answers.unwrap)(answerService.add(id, _))
    } yield id
  }

  def updateQuestion(newQuestion: QuestionForUpdateDto): Future[Option[QuestionWithAnswersDto]] = {
    for {
      question <- questionRepo.findById(newQuestion.id)
      updated <- updateQuestionIfFound(newQuestion.id, question, (q: Question) => newQuestion.toQuestion(q.isActive, q.isMulti))
      updatedAnswers <- Future.flatTraverse(newQuestion.answers)(answerService.update)
    } yield updated.map(QuestionWithAnswersDto(_, updatedAnswers))
  }

  def deActivate(id: Question.Id): Future[Option[Question]] = {
    for {
      question <- questionRepo.findById(id)
      updated <- updateQuestionIfFound(id, question, (_: Question).copy(isActive = Question.IsActive(false)))
    } yield updated
  }

  private def updateQuestionIfFound(qId: Question.Id, question: Option[Question], update: Question => Question) =
    question match {
      case Some(q) => questionRepo.update(qId, update(q)).map(Option(_))
      case None => Future.successful(None)
    }
}
