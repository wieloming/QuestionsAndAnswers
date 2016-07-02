package domain.question

import domain.Id
import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}

case class Question(id: Option[Question.id], text: Question.text, isMulti: Question.isMulti, isActive: Question.isActive)
case object Question {
  case class id(value: Long) extends Id
  case class text(value: String)
  case class isMulti(value: Boolean)
  case class isActive(value: Boolean)
}

case class QuestionWithAnswersDto(question: Question, answers: List[Answer])
case class QuestionForUpdateDto(id: Question.id, text: Question.text, isMulti: Question.isMulti, answers: List[AnswerForUpdateDto]){
  def toQuestion(isActive: Question.isActive): Question = Question(Option(id), text, isMulti, isActive)
}

//TODO: replace answers with NEL
case class QuestionForCreateDto(text: Question.text, isMulti: Question.isMulti, answers: List[AnswerForCreateDto]) {
  def toQuestion: Question = Question(None, text, isMulti, Question.isActive(true))
}
