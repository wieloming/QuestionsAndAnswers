package services.question

import cats.implicits._
import domain.Validated
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
      dto = question.map(QuestionWithAnswersDto(_, answers))
    } yield dto
  }

  def addQuestion(question: QuestionForCreateDto): Future[Question.Id] = {
    //TODO: fix this method
    def validateAnswers() = question.answers.unwrap.foreach(_.toValidAnswer(Question.Id(0)))

    val validated = question.toValidQuestion
    validateAnswers()
    for {
      id <- questionRepo.create(validated)
      answers <- Future.traverse(question.answers.unwrap)(answerService.add(id, _))
    } yield id
  }

  def updateQuestion(newQuestion: QuestionForUpdateDto): Future[Option[QuestionWithAnswersDto]] = {
    val updateModel = (q: Question) => newQuestion.toValidQuestion(q.isActive, q.isMulti)
    for {
      question <- questionRepo.findById(newQuestion.id)
      updated <- updateQuestionIfFound(newQuestion.id, question, updateModel)
      updatedAnswers <- Future.flatTraverse(newQuestion.answers)(answerService.update)
      dto = updated.map(QuestionWithAnswersDto(_, updatedAnswers))
    } yield dto
  }

  def deActivate(id: Question.Id): Future[Option[Question]] = {
    val updateModel = (_: Question).copy(isActive = Question.IsActive(false)).validate
    for {
      question <- questionRepo.findById(id)
      updated <- updateQuestionIfFound(id, question, updateModel)
    } yield updated
  }

  private def updateQuestionIfFound(qId: Question.Id, question: Option[Question], updateModel: Question => Validated[Question]) =
    question match {
      case Some(q) =>
        val validated = updateModel(q)
        questionRepo.update(qId, validated).map(Option(_))
      case None => Future.successful(None)
    }
}
