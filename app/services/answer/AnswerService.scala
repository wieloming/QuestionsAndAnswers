package services.answer

import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import domain.question.Question
import repositories.interfaces.AnswerRepo
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class AnswerService(answerRepo: AnswerRepo) {

  def update(newAnswer: AnswerForUpdateDto): Future[Option[Answer]] = {
    def updateAnswerIfFound(answer: Option[Answer]) = answer match {
      case Some(a) => answerRepo.update(newAnswer.id, newAnswer.toAnswer).map(Option(_))
      case None => Future.successful(None)
    }
    for {
      answer <- answerRepo.findById(newAnswer.id)
      updated <- updateAnswerIfFound(answer)
    } yield updated
  }

  def findById(id: Answer.id): Future[Option[Answer]] = {
    answerRepo.findById(id)
  }

  def findByQuestionId(id: Question.id): Future[List[Answer]] = {
    answerRepo.findByQuestionId(id)
  }

  def add(questionId: Question.id, answer: AnswerForCreateDto) = {
    answerRepo.create(Answer(None, answer.text, questionId, Answer.isActive(true)))
  }

}
