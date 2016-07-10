package domain.answer

import domain.Validated
import domain.exceptions.InvalidFormatException
import domain.question.Question

case class Answer(
                   id: Option[Answer.Id],
                   text: Answer.Text,
                   questionId: Question.Id,
                   isActive: Answer.IsActive
                 ) {
  def validate: Validated[Answer] = {
    if (text.length > 0) Validated(this)
    else throw new InvalidFormatException("Invalid answer text.")
  }
}
case object Answer {
  case class Id(value: Long) extends domain.Id
  case class Text(value: String) {
    def length = value.length
  }
  case class IsActive(value: Boolean)
}

case class AnswerForCreateDto(text: Answer.Text) {
  def toValidAnswer(questionId: Question.Id) =
    Answer(None, text, questionId, Answer.IsActive(true)).validate
}
case class AnswerForUpdateDto(id: Answer.Id, text: Answer.Text, questionId: Question.Id, isActive: Answer.IsActive) {
  def toValidAnswer =
    Answer(Option(id), text, questionId, isActive).validate
}


