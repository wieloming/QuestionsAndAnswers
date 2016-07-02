package repositories.interfaces

import domain.answer.Answer
import domain.question.Question

import scala.concurrent.Future

trait AnswerRepo extends BaseRepo[Answer, Answer.id] {
  def findByQuestionId(id: Question.id): Future[List[Answer]]
}
