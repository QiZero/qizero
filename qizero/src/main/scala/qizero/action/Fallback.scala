package qizero.action

import scala.util.control.NonFatal

trait Fallback extends Invoker {
  _: Action[_] =>

  private var _isResultFromFallback: Boolean = false
  private var _cause: Option[Throwable] = None

  final def isResultFromFallback = _isResultFromFallback
  final def cause = _cause

  protected def fallback(): Result

  abstract override protected def preInvoke(): Unit = {
    _isResultFromFallback = false
    _cause = None
  }

  abstract override protected def aroundInvoke(): Result = {
    try {
      super.aroundInvoke()
    } catch {
      case NonFatal(ex) =>
        _cause = Some(ex)
        _isResultFromFallback = true
        fallback()
    }
  }
}

