package qizero.failure

import scala.util.control.NoStackTrace

class Failure(val message:String) extends RuntimeException(message) with NoStackTrace

object NotImplementedFailure extends Failure("not_implemented_yet")

class UnhandledFailure(val param: Option[Any] = None) extends Failure("unhandled")