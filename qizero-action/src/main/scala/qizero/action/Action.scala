package qizero.action

import qizero.failure.{NotImplementedFailure, Failure}
import scala.concurrent.{ExecutionContext, Future}

trait Invoker {
  _: Action[_] =>
  type Response
  protected def invoke(): Response
}

abstract class Action[R] extends Invoker {
  type Response = R
  protected def act(): R
  protected def invoke(): Response = act()


  protected final def TODO = throw NotImplementedFailure
  protected final def failure(message: String) = throw new Failure(message)

}

object Action {

  implicit final class Runner[+R](action: Action[R]) {
    def run: R = action.invoke()
    def ask(implicit executor: ExecutionContext): Future[R] = Future(run)
    def tell(implicit executor: ExecutionContext): Unit = ask
  }

  implicit final class Mapper[R](action: Action[R]) {
    def map[B](f: R => B): Action[B] = MapAction(action, f)
  }

}

case class MapAction[A, B](action: Action[A], f: A => B) extends Action[B] {
  protected def act(): B = f(action.run)
}