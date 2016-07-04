package utils

import domain.answer.Answer
import domain.question.{Question, QuestionWithAnswersDto}
import domain.user.User
import domain.vote.Vote
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import repositories.implementations.file.answer.AnswerFileRepository
import repositories.implementations.file.question.QuestionFileRepository
import repositories.implementations.file.vote.VoteFileRepository
import services.answer.AnswerService
import services.question.QuestionService
import services.vote.VoteService

import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable}
import scala.language.postfixOps

trait TestContainer {

  val questionRepo = new QuestionFileRepository
  val answerRepo = new AnswerFileRepository
  val voteRepo = new VoteFileRepository

  val answerService = new AnswerService(answerRepo)
  val questionService = new QuestionService(questionRepo, answerService)
  val voteService = new VoteService(voteRepo, questionService, answerService)

  val finiteDuration = 10 seconds
  def await[T](f: Awaitable[T]): T = Await.result(f, finiteDuration)
  val now = DateTime.parse("04/02/2011 20:27:05", DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"))

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

  val userVotes = Map(Question.id(1) -> List(Vote(Option(Vote.id(1)), Question.id(1), Answer.id(1), User.id(1), now)))
  val userAndQuestionVotes = List(Vote(Option(Vote.id(1)), Question.id(1), Answer.id(1), User.id(1), now))
}
