package services.question

import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import domain.question.{Question, QuestionForCreateDto, QuestionForUpdateDto}
import play.api.test.PlaySpecification
import utils.TestContainer

import cats.data.NonEmptyList


class QuestionServiceTest extends PlaySpecification with TestContainer {

  "QuestionService" should {
    "add question" in {
      val question = QuestionForCreateDto(
        Question.Text("text"),
        Question.IsMulti(false),
        NonEmptyList(AnswerForCreateDto(Answer.Text("text?")))
      )
      val createdId = await(questionService.addQuestion(question))
      createdId must equalTo(Question.Id(1))
    }
  }
  "QuestionService" should {
    "find question" in {
      val fromDb = await(questionService.findWithAnswersById(Question.Id(1)))
      fromDb must equalTo(Some(questionWithAnswers))
    }
  }
  "QuestionService" should {
    "update question" in {
      val newQuestion = QuestionForUpdateDto(
        Question.Id(1),
        Question.Text("text2"),
        List(AnswerForUpdateDto(Answer.Id(1), Answer.Text("text?"), Question.Id(1), Answer.IsActive(false)))
      )
      val withAnswers = await(questionService.updateQuestion(newQuestion))
      withAnswers must equalTo(Some(questionWithAnswersUpdated))
    }
  }
  "QuestionService" should {
    "find question after update" in {
      val fromDb = await(questionService.findWithAnswersById(Question.Id(1)))
      fromDb must equalTo(Some(questionWithAnswersUpdated))
    }
  }
  "QuestionService" should {
    "deactivate question" in {
      val fromDb = await(questionService.deActivate(Question.Id(1)))
      fromDb must equalTo(Some(questionNotActive))
    }
  }
}

