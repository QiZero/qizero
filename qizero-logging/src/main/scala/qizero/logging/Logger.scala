package qizero.logging

import org.slf4j.{Logger => SLF4JLogger, LoggerFactory}

object Logger {
  def apply(logger: SLF4JLogger): Logger = new Logger(logger)
  def apply(name: String): Logger = apply(LoggerFactory.getLogger(name))
  def apply[T](clazz: Class[T]): Logger = apply(clazz.getName)
  def apply(obj: AnyRef): Logger = apply(obj.getClass)
}

final class Logger private(underlying: SLF4JLogger) {
  def name: String = underlying.getName

  def isTraceEnabled: Boolean = underlying.isTraceEnabled
  def isDebugEnabled: Boolean = underlying.isDebugEnabled
  def isInfoEnabled: Boolean = underlying.isInfoEnabled
  def isWarnEnabled: Boolean = underlying.isWarnEnabled
  def isErrorEnabled: Boolean = underlying.isErrorEnabled

  def trace(message: => String): Unit = if (isTraceEnabled) underlying.trace(message)
  def trace(message: => String, args: AnyRef*): Unit = if (isTraceEnabled) underlying.trace(message, args: _*)
  def trace(message: => String, cause: Throwable): Unit = if (isTraceEnabled) underlying.trace(message, cause)

  def debug(message: => String): Unit = if (isDebugEnabled) underlying.debug(message)
  def debug(message: => String, args: AnyRef*): Unit = if (isDebugEnabled) underlying.debug(message, args: _*)
  def debug(message: => String, cause: Throwable): Unit = if (isDebugEnabled) underlying.debug(message, cause)

  def info(message: => String): Unit = if (isInfoEnabled) underlying.info(message)
  def info(message: => String, args: AnyRef*): Unit = if (isInfoEnabled) underlying.info(message, args: _*)
  def info(message: => String, cause: Throwable): Unit = if (isInfoEnabled) underlying.info(message, cause)

  def warn(message: => String): Unit = if (isWarnEnabled) underlying.warn(message)
  def warn(message: => String, args: AnyRef*): Unit = if (isWarnEnabled) underlying.warn(message, args: _*)
  def warn(message: => String, cause: Throwable): Unit = if (isWarnEnabled) underlying.warn(message, cause)

  def error(message: => String): Unit = if (isErrorEnabled) underlying.error(message)
  def error(message: => String, args: AnyRef*): Unit = if (isErrorEnabled) underlying.error(message, args: _*)
  def error(message: => String, cause: Throwable): Unit = if (isErrorEnabled) underlying.error(message, cause)

}