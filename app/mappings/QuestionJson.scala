package mappings

import domain.question._
import play.api.libs.json._

trait QuestionJson extends BaseJson with AnswerJson{
  implicit val QuestionTextFormat = Json.format[Question.text]
  implicit val QuestionIsActiveFormat = Json.format[Question.isActive]
  implicit val QuestionIsMultiFormat = Json.format[Question.isMulti]
  implicit val QuestionFormat = Json.format[Question]
  implicit val QuestionForCreateDtoFormat = Json.format[QuestionForCreateDto]
  implicit val QuestionForUpdateDtoFormat = Json.format[QuestionForUpdateDto]
  implicit val QuestionWithAnswersDtoFormat = Json.format[QuestionWithAnswersDto]
}
