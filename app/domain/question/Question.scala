package domain.question

import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import cats.data.NonEmptyList
import domain.Validated
import domain.exceptions.InvalidFormatException

case class Question(
                     id: Option[Question.Id],
                     text: Question.Text,
                     isMulti: Question.IsMulti,
                     isActive: Question.IsActive) {
  def validate: Validated[Question] = {
    if (text.length > 0) Validated(this)
    else throw new InvalidFormatException("Invalid question text.")
  }
}
case object Question {
  case class Id(value: Long) extends domain.Id
  case class Text(value: String) {
    def length = value.length
  }
  case class IsMulti(value: Boolean) extends AnyVal
  case class IsActive(value: Boolean) extends AnyVal
}

case class QuestionWithAnswersDto(
                                   question: Question,
                                   answers: List[Answer]
                                 )
case class QuestionForUpdateDto(
                                 id: Question.Id,
                                 text: Question.Text,
                                 answers: List[AnswerForUpdateDto]
                               ) {
  def toValidQuestion(isActive: Question.IsActive, isMulti: Question.IsMulti) =
    Question(Option(id), text, isMulti, isActive).validate
}

case class QuestionForCreateDto(
                                 text: Question.Text,
                                 isMulti: Question.IsMulti,
                                 answers: NonEmptyList[AnswerForCreateDto]
                               ) {
  def toValidQuestion = Question(None, text, isMulti, Question.IsActive(true)).validate
}
