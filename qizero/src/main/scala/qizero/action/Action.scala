package qizero.action

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}

trait Invoker {
  self: Action[_] =>
  type Result
  protected def invoke(): Result
}

abstract class Action[R] extends Invoker {
  type Result = R

  protected def act(): R
  protected def invoke(): Result = act()

  final def map[B](f: R => B): Action[B] = Action(f(invoke()))
  final def flatMap[B](f: R => Action[B]): Action[B] = Action(f(invoke()).invoke())

  protected final def TODO = throw NotImplementedFailure
  protected final def failure(message: String) = throw new ActionFailure(message)

}

object Action {

  def apply[R](block: => R): Action[R] = new Action[R] {
    protected def act(): R = block
  }

  implicit final class Runner[R](action: Action[R]) {
    def run: R = action.invoke()
    def async(implicit executor: ExecutionContext = global): Future[R] = Future(run)
  }

}

