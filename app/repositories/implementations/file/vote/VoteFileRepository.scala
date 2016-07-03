package repositories.implementations.file.vote

import java.util.concurrent.atomic.AtomicLong

import domain.question.Question
import domain.user.User
import domain.vote.Vote
import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.VoteRepo

import scala.concurrent.Future

class VoteFileRepository extends VoteRepo with BaseFileRepository[Vote, Vote.id] {
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

  def findForUser(id: User.id): Future[List[Vote]] = {
    Future.successful(db.values.filter(_.userId == id).toList)
  }

  def findForQuestionAndUser(questionId: Question.id, userId: User.id): Future[List[Vote]] = {
    Future.successful(db.values.filter(v => v.userId == userId && v.questionId == questionId).toList)
  }

}
