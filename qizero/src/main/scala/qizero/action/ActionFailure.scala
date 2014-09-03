package qizero.action

import scala.util.control.NoStackTrace

class ActionFailure(val message: String) extends RuntimeException(message) with NoStackTrace

object NotImplementedFailure extends ActionFailure("not_implemented_yet")