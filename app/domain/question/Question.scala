package domain.question

import domain.Id

case class Question(id: Option[Question.id], text: Question.text, isMulti: Question.isMulti)
case object Question {
  case class id(value: Long) extends Id
  case class text(value: String)
  case class isMulti(value: Boolean)
}
