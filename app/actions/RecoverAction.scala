package actions


import domain.exceptions.InvalidFormatException
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RecoverAction extends ActionBuilder[Request] {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    val result = Future[Future[Result]](block(request)).flatMap(identity)
    result.recover { case e => Logger.error("Error ", e) }
    result.recover {
      case InvalidFormatException(m) => Conflict(m)
    }
  }
}

object RecoverAction {
  def apply(): RecoverAction = new RecoverAction()
}
