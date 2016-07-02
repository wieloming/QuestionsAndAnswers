package services.question

import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import domain.question.{Question, QuestionForCreateDto, QuestionForUpdateDto}
import play.api.test.PlaySpecification
import utils.TestContainer


class QuestionServiceTest extends PlaySpecification with TestContainer {

  "QuestionService" should {
    "add question" in {
      val question = QuestionForCreateDto(
        Question.text("text"),
        Question.isMulti(false),
        List(AnswerForCreateDto(Answer.text("text?")))
      )
      val createdId = await(questionService.addQuestion(question))
      createdId must equalTo(Question.id(1))
    }
  }
  "QuestionService" should {
    "find question" in {
      val fromDb = await(questionService.findWithAnswersById(Question.id(1)))
      fromDb must equalTo(Some(questionWithAnswers))
    }
  }
  "QuestionService" should {
    "update question" in {
      val newQuestion = QuestionForUpdateDto(
        Question.id(1),
        Question.text("text2"),
        Question.isMulti(false),
        List(AnswerForUpdateDto(Answer.id(1), Answer.text("text?"), Question.id(1)))
      )
      val withAnswers = await(questionService.updateQuestion(newQuestion))
      withAnswers must equalTo(Some(questionWithAnswersUpdated))
    }
  }
  "QuestionService" should {
    "find question after update" in {
      val fromDb = await(questionService.findWithAnswersById(Question.id(1)))
      fromDb must equalTo(Some(questionWithAnswersUpdated))
    }
  }
  "QuestionService" should {
    "deactivate question" in {
      val fromDb = await(questionService.deActivate(Question.id(1)))
      fromDb must equalTo(Some(questionNotActive))
    }
  }
}

