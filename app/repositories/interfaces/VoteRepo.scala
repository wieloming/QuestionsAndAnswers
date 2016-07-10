package repositories.interfaces

import domain.question.Question
import domain.user.User
import domain.vote.Vote

import scala.concurrent.Future

trait VoteRepo extends BaseRepo[Vote, Vote.Id] {
  def findForQuestionAndUser(questionId: Question.Id, userId: User.Id): Future[List[Vote]]

  def findForUser(id: User.Id): Future[List[Vote]]
}
