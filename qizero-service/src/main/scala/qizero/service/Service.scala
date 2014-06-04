package qizero.service

import akka.actor._
import qizero.action.Action
import qizero.failure._

trait Service extends Actor {

  implicit class ActorRunner[R](action: Action[R]) {
    final def reply(implicit context: ActorContext): Unit = {
      implicit val dispatcher = context.dispatcher
      akka.pattern.pipe(action.ask).pipeTo(context.sender)
    }
  }

  protected final def TODO = {
    sender() ! Status.Failure(NotImplementedFailure)
  }

  override def unhandled(message: Any): Unit = {
    sender() ! Status.Failure(new UnhandledFailure(message))
    super.unhandled(message)
  }
}

