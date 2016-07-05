package domain.question

import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import cats.data.NonEmptyList

case class Question(
                     id: Option[Question.Id],
                     text: Question.Text,
                     isMulti: Question.IsMulti,
                     isActive: Question.IsActive)
case object Question {
  case class Id(value: Long) extends domain.Id
  case class Text(value: String)
  case class IsMulti(value: Boolean)
  case class IsActive(value: Boolean)
}

case class QuestionWithAnswersDto(
                                   question: Question,
                                   answers: NonEmptyList[Answer]
                                 )
case class QuestionForUpdateDto(
                                 id: Question.Id,
                                 text: Question.Text,
                                 answers: NonEmptyList[AnswerForUpdateDto]
                               ) {
  def toQuestion(isActive: Question.IsActive, isMulti: Question.IsMulti): Question = Question(Option(id), text, isMulti, isActive)
}

case class QuestionForCreateDto(
                                 text: Question.Text,
                                 isMulti: Question.IsMulti,
                                 answers: NonEmptyList[AnswerForCreateDto]
                               ) {
  def toQuestion: Question = Question(None, text, isMulti, Question.IsActive(true))
}
