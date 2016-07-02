package repositories.implementations.file.question

import java.util.concurrent.atomic.AtomicLong

import domain.question.Question
import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.QuestionRepo

import scala.concurrent.Future

class QuestionFileRepository extends QuestionRepo with BaseFileRepository[Question, Question.id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Question.id, Question]()

  def create(obj: Question): Future[Question.id] = {
    val newId = Question.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Question.id, obj: Question): Future[Question] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
