package qizero.action

trait Fallback extends Invoker {
  _: Action[_] =>

  @volatile private var isFallback: Boolean = false

  final def isResultFromFallback: Boolean = isFallback

  protected def fallback(): Result

  abstract override protected def invoke(): Result = {
    try {
      isFallback = false
      super.invoke()
    } catch {
      case ex: Throwable =>
        isFallback = true
        fallback()
    }
  }
}

