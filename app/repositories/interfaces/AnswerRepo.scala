package repositories.interfaces

import domain.answer.Answer
import domain.question.Question

import scala.concurrent.Future

trait AnswerRepo extends BaseRepo[Answer, Answer.Id] {
  def findByQuestionId(id: Question.Id): Future[List[Answer]]
}
