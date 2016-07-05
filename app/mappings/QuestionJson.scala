package mappings

import cats.data._
import domain.answer.Answer
import domain.question._
import play.api.libs.json._

trait QuestionJson extends BaseJson with AnswerJson{

  implicit def jsonNELWrites[T: Writes] = new Writes[NonEmptyList[T]] {
    def writes(o: NonEmptyList[T]): JsValue = {
      val keyAsString = o.head :: o.tail
      Json.toJson(keyAsString)
    }
  }
  implicit def mapReads[T: Reads]: Reads[NonEmptyList[T]] = new Reads[NonEmptyList[T]] {
    def reads(jv: JsValue): JsResult[NonEmptyList[T]] = {
      val list = jv.as[List[T]]
      JsSuccess(NonEmptyList(list.head, list.tail))
    }
  }
  implicit val mapFormat: Format[NonEmptyList[Answer]] = Format(mapReads, jsonNELWrites)

  implicit val QuestionTextFormat = Json.format[Question.Text]
  implicit val QuestionIsActiveFormat = Json.format[Question.IsActive]
  implicit val QuestionIsMultiFormat = Json.format[Question.IsMulti]
  implicit val QuestionFormat = Json.format[Question]
  implicit val QuestionForCreateDtoFormat = Json.format[QuestionForCreateDto]
  implicit val QuestionForUpdateDtoFormat = Json.format[QuestionForUpdateDto]
  implicit val QuestionWithAnswersDtoFormat = Json.format[QuestionWithAnswersDto]


}
