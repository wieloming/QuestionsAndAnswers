package repositories.implementations.inMemory.question

import java.util.concurrent.atomic.AtomicLong

import domain.question.Question
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.QuestionRepo

import scala.concurrent.Future

class QuestionInMemoryRepository extends QuestionRepo with BaseInMemoryRepository[Question, Question.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Question.Id, Question]()

  def create(obj: Question): Future[Question.Id] = {
    val newId = Question.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Question.Id, obj: Question): Future[Question] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
