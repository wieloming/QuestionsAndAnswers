package services

import javax.inject.Singleton

import repositories.implementations.file.answer.AnswerFileRepository
import repositories.implementations.file.question.QuestionFileRepository
import services.answer.AnswerService
import services.question.QuestionService

@Singleton
class Container {
  val questionRepo = new QuestionFileRepository
  val answerRepo = new AnswerFileRepository

  val answerService = new AnswerService(answerRepo)
  val questionService = new QuestionService(questionRepo, answerService)
}
