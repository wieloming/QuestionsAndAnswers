package mappings

import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import domain.question.Question
import play.api.libs.json._

trait AnswerJson extends BaseJson {

  implicit val QuestionIdFormat = Json.format[Question.Id]
  implicit val AnswerIdFormat = Json.format[Answer.Id]
  implicit val AnswerTextFormat = Json.format[Answer.Text]
  implicit val AnswerIsActiveFormat = Json.format[Answer.IsActive]
  implicit val AnswerFormat = Json.format[Answer]
  implicit val AnswerForCreateDtoFormat = Json.format[AnswerForCreateDto]
  implicit val AnswerForUpdateDtoFormat = Json.format[AnswerForUpdateDto]
}
