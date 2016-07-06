package domain.vote

import domain.{Id, Validated}
import domain.user.User
import domain.answer.Answer
import domain.question.Question
import org.joda.time.DateTime

case class Vote(
                 id: Option[Vote.id],
                 questionId: Question.Id,
                 answerId: Answer.Id,
                 userId: User.Id,
                 time: DateTime
               ) {
  def validate: Validated[Vote] = Validated(this)
}
case object Vote {
  case class id(value: Long) extends Id
}
