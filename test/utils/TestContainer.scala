package utils

import domain.answer.Answer
import domain.question.{Question, QuestionWithAnswersDto}
import repositories.implementations.file.answer.AnswerFileRepository
import repositories.implementations.file.question.QuestionFileRepository
import services.answer.AnswerService
import services.question.QuestionService

import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable}

trait TestContainer {

  val questionRepo = new QuestionFileRepository
  val answerRepo = new AnswerFileRepository

  val answerService = new AnswerService(answerRepo)
  val questionService = new QuestionService(questionRepo, answerService)

  val finiteDuration = 10 seconds
  def await[T](f: Awaitable[T]): T = Await.result(f, finiteDuration)


  val questionWithAnswers = QuestionWithAnswersDto(
    Question(
      Some(Question.id(1)),
      Question.text("text"),
      Question.isMulti(false),
      Question.isActive(true)
    ),
    List(Answer(Some(Answer.id(1)), Answer.text("text?"), Question.id(1), Answer.isActive(true)))
  )
  val questionWithAnswersUpdated = QuestionWithAnswersDto(
    Question(
      Some(Question.id(1)),
      Question.text("text2"),
      Question.isMulti(false),
      Question.isActive(true)
    ),
    List(Answer(Some(Answer.id(1)), Answer.text("text?"), Question.id(1), Answer.isActive(false)))
  )
  val questionNotActive = Question(
    Some(Question.id(1)),
    Question.text("text2"),
    Question.isMulti(false),
    Question.isActive(false)
  )
}
