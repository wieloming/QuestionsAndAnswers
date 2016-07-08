package services.question

import cats.data.NonEmptyList
import domain.answer.{Answer, AnswerForCreateDto, AnswerForUpdateDto}
import domain.exceptions.InvalidFormatException
import domain.question.{Question, QuestionForCreateDto, QuestionForUpdateDto}
import play.api.test.PlaySpecification
import utils.TestContainer

import scala.util.{Failure, Try}


class QuestionServiceTest extends PlaySpecification with TestContainer {

  "QuestionService" should {
    "throw exception on empty text on add" in {
      val question = QuestionForCreateDto(
        Question.Text(""),
        Question.IsMulti(false),
        NonEmptyList(AnswerForCreateDto(Answer.Text("text?")))
      )
      val result = Try(await(questionService.addQuestion(question)))
      result must equalTo(Failure(InvalidFormatException("Invalid question text.")))
    }
  }
  "QuestionService" should {
    "throw exception on empty answer text on add" in {
      val question = QuestionForCreateDto(
        Question.Text("test"),
        Question.IsMulti(false),
        NonEmptyList(AnswerForCreateDto(Answer.Text("")))
      )
      val result = Try(await(questionService.addQuestion(question)))
      result must equalTo(Failure(InvalidFormatException("Invalid answer text.")))
    }
  }
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
    "throw exception on empty text on update" in {
      val newQuestion = QuestionForUpdateDto(
        Question.Id(1),
        Question.Text(""),
        List(AnswerForUpdateDto(Answer.Id(1), Answer.Text("text?"), Question.Id(1), Answer.IsActive(false)))
      )
      val result = Try(await(questionService.updateQuestion(newQuestion)))
      result must equalTo(Failure(InvalidFormatException("Invalid question text.")))
    }
  }
  "QuestionService" should {
    "throw exception on empty answer text on update" in {
      val newQuestion = QuestionForUpdateDto(
        Question.Id(1),
        Question.Text("text"),
        List(AnswerForUpdateDto(Answer.Id(1), Answer.Text(""), Question.Id(1), Answer.IsActive(false)))
      )
      val result = Try(await(questionService.updateQuestion(newQuestion)))
      result must equalTo(Failure(InvalidFormatException("Invalid answer text.")))
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

