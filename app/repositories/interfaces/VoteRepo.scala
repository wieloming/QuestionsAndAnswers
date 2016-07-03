package repositories.interfaces

import domain.question.Question
import domain.user.User
import domain.vote.Vote

import scala.concurrent.Future

trait VoteRepo extends BaseRepo[Vote, Vote.id] {
  def findForQuestionAndUser(questionId: Question.id, userId: User.id): Future[List[Vote]]

  def findForUser(id: User.id): Future[List[Vote]]
}
