package services.question

import domain.question.{Question, QuestionForCreateDto, QuestionForUpdateDto, QuestionWithAnswersDto}
import repositories.interfaces.QuestionRepo
import services.answer.AnswerService
import utils.Futures._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class QuestionService(questionRepo: QuestionRepo, answerService: AnswerService) {

  def findWithAnswersById(id: Question.id): Future[Option[QuestionWithAnswersDto]] = {
    for {
      question <- questionRepo.findById(id)
      answers <- answerService.findByQuestionId(id)
    } yield question.map(QuestionWithAnswersDto(_, answers))
  }

  def addQuestion(question: QuestionForCreateDto) = {
    for{
      id <- questionRepo.create(question.toQuestion)
      answers <- Future.traverse(question.answers)(answerService.add(id, _))
    } yield id
  }

  def updateQuestion(newQuestion: QuestionForUpdateDto): Future[Option[QuestionWithAnswersDto]] = {
    def updateQuestionIfFound(question: Option[Question]) = question match {
      case Some(q) => questionRepo.update(newQuestion.id, newQuestion.toQuestion(q.isActive)).map(Option(_))
      case None => Future.successful(None)
    }
    for {
      question <- questionRepo.findById(newQuestion.id)
      updated <- updateQuestionIfFound(question)
      updatedAnswers <- Future.flatTraverse(newQuestion.answers)(answerService.update)
    } yield updated.map(QuestionWithAnswersDto(_, updatedAnswers))
  }

  def deActivate(id: Question.id): Future[Option[Question]] = {
    def updateIfFound(question: Option[Question]) = question match {
      case Some(q) => questionRepo.update(id, q.copy(isActive = Question.isActive(false))).map(Option(_))
      case None => Future.successful(None)
    }
    for {
      question <- questionRepo.findById(id)
      updated <- updateIfFound(question)
    } yield updated
  }
}
