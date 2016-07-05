package domain.answer

import domain.question.Question

case class Answer(id: Option[Answer.Id], text: Answer.Text, questionId: Question.Id, isActive: Answer.IsActive)
case object Answer {
  case class Id(value: Long) extends domain.Id
  case class Text(value: String)
  case class IsActive(value: Boolean)
}

case class AnswerForCreateDto(text: Answer.Text)
case class AnswerForUpdateDto(id: Answer.Id, text: Answer.Text, questionId: Question.Id, isActive: Answer.IsActive) {
  def toAnswer: Answer = Answer(Option(id), text, questionId, isActive)
}


