package repositories.implementations.inMemory.vote

import java.util.concurrent.atomic.AtomicLong

import domain.Validated
import domain.question.Question
import domain.user.User
import domain.vote.Vote
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.VoteRepo

import scala.concurrent.Future

class VoteInMemoryRepository extends VoteRepo with BaseInMemoryRepository[Vote, Vote.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Vote.Id, Vote]()

  def create(obj: Validated[Vote]): Future[Vote.Id] = {
    val extracted = obj.value
    val newId = Vote.Id(idSequence.incrementAndGet())
    val newObj = extracted.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Vote.Id, obj: Validated[Vote]): Future[Vote] = {
    val extracted = obj.value
    val newObj = extracted.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }

  def findForUser(id: User.Id): Future[List[Vote]] = {
    Future.successful(db.values.filter(_.userId == id).toList)
  }

  def findForQuestionAndUser(questionId: Question.Id, userId: User.Id): Future[List[Vote]] = {
    Future.successful(db.values.filter(v => v.userId == userId && v.questionId == questionId).toList)
  }
}
