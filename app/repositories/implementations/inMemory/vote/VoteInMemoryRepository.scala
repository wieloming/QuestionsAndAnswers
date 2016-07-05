package repositories.implementations.inMemory.vote

import java.util.concurrent.atomic.AtomicLong

import domain.question.Question
import domain.user.User
import domain.vote.Vote
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.VoteRepo

import scala.concurrent.Future

class VoteInMemoryRepository extends VoteRepo with BaseInMemoryRepository[Vote, Vote.id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Vote.id, Vote]()

  def create(obj: Vote): Future[Vote.id] = {
    val newId = Vote.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Vote.id, obj: Vote): Future[Vote] = {
    val newObj = obj.copy(id = Some(id))
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