package repositories.implementations.inMemory.answer

import java.util.concurrent.atomic.AtomicLong

import domain.Validated
import domain.answer.Answer
import domain.question.Question
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.AnswerRepo

import scala.concurrent.Future

class AnswerInMemoryRepository extends AnswerRepo with BaseInMemoryRepository[Answer, Answer.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Answer.Id, Answer]()

  def create(obj: Validated[Answer]): Future[Answer.Id] = {
    val extracted = obj.value
    val newId = Answer.Id(idSequence.incrementAndGet())
    val newObj = extracted.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Answer.Id, obj: Validated[Answer]): Future[Answer] = {
    val extracted = obj.value
    val newObj = extracted.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
  override def findByQuestionId(id: Question.Id): Future[List[Answer]] = {
    Future.successful(db.values.filter(_.questionId == id).toList)
  }
}
