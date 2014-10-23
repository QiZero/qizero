package qizero.action

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}
import qizero.logging.Logging

trait Invoker extends Logging {
  self: Action[_] =>
  type Result

  protected def invoke(): Result

  protected def preInvoke(): Unit
  protected def postInvoke(): Unit

  protected def aroundInvoke(): Result
  protected def aroundPreInvoke(): Unit
  protected def aroundPostInvoke(): Unit

}

abstract class Action[R] extends Invoker{
  type Result = R

  protected def act(): R
  protected def invoke(): Result = act()

  protected def preInvoke():Unit = ()
  protected def postInvoke():Unit = ()

  protected def aroundInvoke(): Result = invoke()
  protected def aroundPreInvoke(): Unit = preInvoke()
  protected def aroundPostInvoke(): Unit = postInvoke()


  final def map[B](f: R => B): Action[B] = Action(f(this.run))
  final def flatMap[B](f: R => Action[B]): Action[B] = Action(f(this.run).run)

  protected final def TODO = throw NotImplementedFailure
  protected final def failure(message: String) = throw new ActionFailure(message)

}

object Action {

  def apply[R](block: => R): Action[R] = new Action[R] {
    protected def act(): R = block
  }

  implicit final class Runner[R](action: Action[R]) {
    def run: R = {
      action.aroundPreInvoke()
      val result = action.aroundInvoke()
      action.aroundPostInvoke()
      result
    }

    def async(implicit executor: ExecutionContext = global): Future[R] = Future(run)
  }

}

