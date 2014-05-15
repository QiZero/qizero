package qizero.failure

import scala.util.control.NoStackTrace

class Failure(val message: String) extends RuntimeException(message) with NoStackTrace

object NotImplementedFailure extends Failure("not_implemented_yet")

class UnhandledFailure(val params: Any*) extends Failure("unhandled")