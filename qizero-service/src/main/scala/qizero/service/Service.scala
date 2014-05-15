package qizero.service

import akka.actor._
import akka.util.Timeout
import qizero.action.Action
import qizero.failure.{UnhandledFailure, NotImplementedFailure}
import qizero.message.{Reply, Message}
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
import scala.reflect.ClassTag

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

  implicit def toRef[M <: Message](actorRef: ActorRef) = new ServiceRef[M](actorRef)

}

trait ServiceSystem {
  implicit def system: ActorSystem

  final def service[M <: Message](props: Props, name: String): ServiceRef[M] = {
    system.actorOf(props, name)
  }
}