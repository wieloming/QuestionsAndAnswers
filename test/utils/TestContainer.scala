package utils

import domain.answer.Answer
import domain.question.{Question, QuestionWithAnswersDto}
import domain.user.User
import domain.vote.Vote
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import repositories.implementations.inMemory.answer.AnswerInMemoryRepository
import repositories.implementations.inMemory.question.QuestionInMemoryRepository
import repositories.implementations.inMemory.vote.VoteInMemoryRepository
import services.answer.AnswerService
import services.question.QuestionService
import services.vote.VoteService

import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable}
import scala.language.postfixOps

trait TestContainer {

  val questionRepo = new QuestionInMemoryRepository
  val answerRepo = new AnswerInMemoryRepository
  val voteRepo = new VoteInMemoryRepository

  val answerService = new AnswerService(answerRepo)
  val questionService = new QuestionService(questionRepo, answerService)
  val voteService = new VoteService(voteRepo, questionService, answerService)

  val finiteDuration = 10 seconds
  def await[T](f: Awaitable[T]): T = Await.result(f, finiteDuration)
  val now = DateTime.parse("04/02/2011 20:27:05", DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"))

  val questionWithAnswers = QuestionWithAnswersDto(
    Question(
      Some(Question.Id(1)),
      Question.Text("text"),
      Question.IsMulti(false),
      Question.IsActive(true)
    ),
    List(Answer(Some(Answer.Id(1)), Answer.Text("text?"), Question.Id(1), Answer.IsActive(true)))
  )
  val questionWithAnswersUpdated = QuestionWithAnswersDto(
    Question(
      Some(Question.Id(1)),
      Question.Text("text2"),
      Question.IsMulti(false),
      Question.IsActive(true)
    ),
    List(Answer(Some(Answer.Id(1)), Answer.Text("text?"), Question.Id(1), Answer.IsActive(false)))
  )
  val questionNotActive = Question(
    Some(Question.Id(1)),
    Question.Text("text2"),
    Question.IsMulti(false),
    Question.IsActive(false)
  )

  val userVotes = Map(Question.Id(1) -> List(Vote(Option(Vote.Id(1)), Question.Id(1), Answer.Id(1), User.Id(1), now)))
  val userAndQuestionVotes = List(Vote(Option(Vote.Id(1)), Question.Id(1), Answer.Id(1), User.Id(1), now))
}
