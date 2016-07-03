package services

import javax.inject.Singleton

import repositories.implementations.file.answer.AnswerFileRepository
import repositories.implementations.file.question.QuestionFileRepository
import repositories.implementations.file.vote.VoteFileRepository
import services.answer.AnswerService
import services.question.QuestionService
import services.vote.VoteService

@Singleton
class Container {
  val questionRepo = new QuestionFileRepository
  val answerRepo = new AnswerFileRepository
  val voteRepo = new VoteFileRepository

  val answerService = new AnswerService(answerRepo)
  val questionService = new QuestionService(questionRepo, answerService)
  val voteService = new VoteService(voteRepo, questionService, answerService)
}
