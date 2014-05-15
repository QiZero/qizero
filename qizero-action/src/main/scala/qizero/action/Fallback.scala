package qizero.action

trait Fallback extends Invoker {
  _: Action[_] =>

  @volatile private var _isFallback: Boolean = false

  final def isResponseFromFallback: Boolean = _isFallback

  protected def fallback(): Result

  abstract override protected def invoke(): Result = {
    try {
      _isFallback = false
      super.invoke()
    } catch {
      case ex: Throwable =>
        _isFallback = true
        fallback()
    }
  }
}

