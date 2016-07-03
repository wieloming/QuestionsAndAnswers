package domain.vote

import domain.Id
import domain.user.User
import domain.answer.Answer
import domain.question.Question
import org.joda.time.DateTime

case class Vote(id: Option[Vote.id], questionId: Question.id, answerId: Answer.id, userId: User.id, time: DateTime)
case object Vote {
  case class id(value: Long) extends Id
}
