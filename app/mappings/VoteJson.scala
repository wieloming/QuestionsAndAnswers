package mappings

import domain.answer.Answer
import domain.question.Question
import domain.user.User
import domain.vote.Vote
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

trait VoteJson extends BaseJson {

  implicit val QuestionIdFormat = Json.format[Question.id]
  implicit val AnswerIdFormat = Json.format[Answer.id]
  implicit val VoteIdFormat = Json.format[Vote.id]
  implicit val UserIdFormat = Json.format[User.id]
  implicit val VoteFormat = Json.format[Vote]

  implicit val jsonWrites = new Writes[Map[Question.id, List[Vote]]] {
    def writes(o: Map[Question.id, List[Vote]]): JsValue = {
      val keyAsString = o.map { kv => kv._1.toString -> kv._2} // Convert to Map[String,Int] which it can convert
      Json.toJson(keyAsString)
    }
  }

}
