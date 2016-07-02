package repositories.interfaces

import domain.question.Question

trait QuestionRepo extends BaseRepo[Question, Question.id] {

}
