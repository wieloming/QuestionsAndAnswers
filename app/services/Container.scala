package services

import javax.inject.Singleton

import repositories.implementations.inMemory.answer.AnswerInMemoryRepository
import repositories.implementations.inMemory.question.QuestionInMemoryRepository
import repositories.implementations.inMemory.vote.VoteInMemoryRepository
import services.answer.AnswerService
import services.question.QuestionService
import services.vote.VoteService

@Singleton
class Container {
  val questionRepo = new QuestionInMemoryRepository
  val answerRepo = new AnswerInMemoryRepository
  val voteRepo = new VoteInMemoryRepository

  val answerService = new AnswerService(answerRepo)
  val questionService = new QuestionService(questionRepo, answerService)
  val voteService = new VoteService(voteRepo, questionService, answerService)
}
