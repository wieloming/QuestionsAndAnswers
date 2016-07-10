package domain.vote

import domain.Validated
import domain.answer.Answer
import domain.question.Question
import domain.user.User
import org.joda.time.DateTime

case class Vote(
                 id: Option[Vote.Id],
                 questionId: Question.Id,
                 answerId: Answer.Id,
                 userId: User.Id,
                 time: DateTime
               ) {
  def validate: Validated[Vote] = Validated(this)
}
case object Vote {
  case class Id(value: Long) extends domain.Id
}
