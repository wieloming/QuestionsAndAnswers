package repositories.implementations.file.answer

import java.util.concurrent.atomic.AtomicLong

import domain.answer.Answer
import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.AnswerRepo

import scala.concurrent.Future

class AnswerFileRepository extends AnswerRepo with BaseFileRepository[Answer, Answer.id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Answer.id, Answer]()

  def create(obj: Answer): Future[Answer.id] = {
    val newId = Answer.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Answer.id, obj: Answer): Future[Answer] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
