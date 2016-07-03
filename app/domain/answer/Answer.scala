package domain.answer

import domain.Id
import domain.question.Question

case class Answer(id: Option[Answer.id], text: Answer.text, questionId: Question.id, isActive: Answer.isActive)
case object Answer {
  case class id(value: Long) extends Id
  case class text(value: String)
  case class isActive(value: Boolean)
}

case class AnswerForCreateDto(text: Answer.text)
case class AnswerForUpdateDto(id: Answer.id, text: Answer.text, questionId: Question.id, isActive: Answer.isActive) {
  def toAnswer: Answer = Answer(Option(id), text, questionId, isActive)
}


