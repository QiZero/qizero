package qizero.service

import akka.actor._
import qizero.action.Action

trait Service extends Actor {

  implicit class ActorRunner[R](action: Action[R]) {
    final def reply(implicit context: ActorContext): Unit = {
      implicit val dispatcher = context.dispatcher
      akka.pattern.pipe(action.async).pipeTo(context.sender)
    }
  }

}

