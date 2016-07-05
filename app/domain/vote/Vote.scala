package domain.vote

import domain.Id
import domain.user.User
import domain.answer.Answer
import domain.question.Question
import org.joda.time.DateTime

case class Vote(id: Option[Vote.id], questionId: Question.Id, answerId: Answer.Id, userId: User.Id, time: DateTime)
case object Vote {
  case class id(value: Long) extends Id
}
