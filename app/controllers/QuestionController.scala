package controllers

import javax.inject.{Singleton, _}

import domain.question.{Question, QuestionForCreateDto, QuestionForUpdateDto}
import mappings.{AnswerJson, QuestionJson}
import play.api.mvc._
import services.Container

@Singleton
class QuestionController @Inject()(container: Container) extends BaseController with AnswerJson with QuestionJson {

  def createQuestion() = Action.async(parse.json[QuestionForCreateDto]) { request =>
    container.questionService.addQuestion(request.body)
  }

  def findQuestionById(id: Long) = Action.async {
    container.questionService.findWithAnswersById(Question.id(id))
  }

  def updateQuestion() = Action.async(parse.json[QuestionForUpdateDto]) { request =>
    container.questionService.updateQuestion(request.body)
  }

  def deactivateQuestion(id: Long) = Action.async { request =>
    container.questionService.deActivate(Question.id(id))
  }
}
