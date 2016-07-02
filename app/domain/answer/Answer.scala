package domain.answer

import domain.Id
import domain.question.Question

case class Answer(id: Option[Answer.id], text: Answer.text, questionId: Question.id)
case object Answer {
  case class id(value: Long) extends Id
  case class text(value: String)
}


