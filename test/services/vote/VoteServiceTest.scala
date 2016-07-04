package services.vote

import domain.answer.{Answer, AnswerForCreateDto}
import domain.question.{Question, QuestionForCreateDto}
import domain.user.User
import domain.vote.Vote
import play.api.test.PlaySpecification
import utils.TestContainer

class VoteServiceTest extends PlaySpecification with TestContainer {

  "VoteService" should {
    "add vote" in {
      val question = QuestionForCreateDto(
        Question.text("text"),
        Question.isMulti(false),
        List(AnswerForCreateDto(Answer.text("text?")))
      )
      //add question
      await(questionService.addQuestion(question))

      val vote = Vote(None, Question.id(1), Answer.id(1), User.id(1), now)
      val createdId = await(voteService.add(vote))
      createdId must equalTo(Some(Vote.id(1)))
    }
  }
  "VoteService" should {
    "find votes for user" in {
      val fromDb = await(voteService.findForUser(User.id(1)))
      fromDb must equalTo(userVotes)
    }
  }
  "VoteService" should {
    "find votes for user and question" in {
      val fromDb = await(voteService.findForQuestionAndUser(Question.id(1), User.id(1)))
      fromDb must equalTo(userAndQuestionVotes)
    }
  }
}

