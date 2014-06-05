package qizero.service

import akka.actor._
import akka.util.Timeout
import qizero.message.{Reply, Message}
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
import scala.reflect.ClassTag

final class ServiceRef[M <: Message](ref: ActorRef) {
  def path: ActorPath = ref.path

  def tell(msg: M, sender: ActorRef): Unit = ref.tell(msg, sender)

  def ask[R: ClassTag](msg: M with Reply[R], timeout: Timeout = ServiceRef.defaultTimeout): Future[R] = {
    akka.pattern.ask(ref, msg)(timeout).mapTo[R]
  }

  def pipe[T](future: Future[T])(implicit executor: ExecutionContext) = {
    akka.pattern.pipe(future)(executor).to(ref)
  }

  def forward(message: M)(implicit context: ActorContext) = tell(message, context.sender)

  def !(message: M)(implicit sender: ActorRef = Actor.noSender): Unit = tell(message, sender)

  def ?[R: ClassTag](message: M with Reply[R]): Future[R] = ask(message)
}

object ServiceRef {
  private val defaultTimeout: Timeout = 15.seconds

  def apply[M <: Message](actorRef: ActorRef) = new ServiceRef[M](actorRef)

  implicit def toRef[M <:Message](actorRef:ActorRef) = new ServiceRef[M](actorRef)

}