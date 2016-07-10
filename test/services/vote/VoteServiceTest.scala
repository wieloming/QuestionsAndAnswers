package services.vote

import domain.answer.{Answer, AnswerForCreateDto}
import domain.question.{Question, QuestionForCreateDto}
import domain.user.User
import domain.vote.Vote
import play.api.test.PlaySpecification
import utils.TestContainer

import cats.data.NonEmptyList

class VoteServiceTest extends PlaySpecification with TestContainer {

  "VoteService" should {
    "add vote" in {
      val question = QuestionForCreateDto(
        Question.Text("text"),
        Question.IsMulti(false),
        NonEmptyList(AnswerForCreateDto(Answer.Text("text?")))
      )
      //add question
      await(questionService.addQuestion(question))

      val vote = Vote(None, Question.Id(1), Answer.Id(1), User.Id(1), now)
      val createdId = await(voteService.add(vote))
      createdId must equalTo(Some(Vote.Id(1)))
    }
  }
  "VoteService" should {
    "find votes for user" in {
      val fromDb = await(voteService.findForUser(User.Id(1)))
      fromDb must equalTo(userVotes)
    }
  }
  "VoteService" should {
    "find votes for user and question" in {
      val fromDb = await(voteService.findForQuestionAndUser(Question.Id(1), User.Id(1)))
      fromDb must equalTo(userAndQuestionVotes)
    }
  }
}

