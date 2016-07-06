package controllers

import javax.inject._

import domain.question.Question
import domain.user.User
import domain.vote.Vote
import mappings.VoteJson
import play.api.mvc._
import services.Container

@Singleton
class VoteController @Inject()(container: Container) extends BaseController with VoteJson {

  def add() = Action.async(parse.json[Vote]) { request =>
    container.voteService.add(request.body)
  }

  def findForUser(id: Long) = Action.async {
    container.voteService.findForUser(User.Id(id))
  }

  def findForQuestionAndUser(questionId: Long, userId: Long) = Action.async {
    container.voteService.findForQuestionAndUser(Question.Id(questionId), User.Id(userId))
  }
}
