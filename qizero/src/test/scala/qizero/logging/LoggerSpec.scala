package qizero.logging

import java.util.Date
import org.mockito.Mockito._
import org.scalatest.{Matchers, WordSpec}
import org.slf4j.{Logger => SLF4JLogger}

class LoggerSpec extends WordSpec with Matchers {

  trait WithLogger {
    val message = "String message {} {} {}!"
    val cause = new Exception("Exception")
    val args = List("1", "2", new Date(3))
    val underlying = mock(classOf[SLF4JLogger])
    val logger = Logger(underlying)
  }

  "trace" when {
    "get level" should {
      "return true if the underlying level is enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(true)
        logger.isTraceEnabled should be(true)
      }
      "return false if the underlying level is not enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(false)
        logger.isTraceEnabled should be(false)
      }
    }
    "calling with a message" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(true)
        logger.trace(message)
        verify(underlying).trace(message)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(false)
        logger.trace(message)
        verify(underlying, never).trace(message)
      }
    }
    "calling with a message and parameters" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(true)
        logger.trace(message, args: _*)
        verify(underlying).trace(message, args: _*)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(false)
        logger.trace(message, args: _*)
        verify(underlying, never).trace(message, args: _*)
      }
    }
    "calling with a message and cause" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(true)
        logger.trace(message, cause)
        verify(underlying).trace(message, cause)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isTraceEnabled).thenReturn(false)
        logger.trace(message, cause)
        verify(underlying, never).trace(message, cause)
      }
    }
  }

  "debug" when {
    "get level" should {
      "return true if the underlying level is enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(true)
        logger.isDebugEnabled should be(true)
      }
      "return false if the underlying level is not enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(false)
        logger.isDebugEnabled should be(false)
      }
    }
    "calling with a message" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(true)
        logger.debug(message)
        verify(underlying).debug(message)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(false)
        logger.debug(message)
        verify(underlying, never).debug(message)
      }
    }
    "calling with a message and parameters" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(true)
        logger.debug(message, args: _*)
        verify(underlying).debug(message, args: _*)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(false)
        logger.debug(message, args: _*)
        verify(underlying, never).debug(message, args: _*)
      }
    }
    "calling with a message and cause" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(true)
        logger.debug(message, cause)
        verify(underlying).debug(message, cause)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isDebugEnabled).thenReturn(false)
        logger.debug(message, cause)
        verify(underlying, never).debug(message, cause)
      }
    }
  }

  "info" when {
    "get level" should {
      "return true if the underlying level is enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(true)
        logger.isInfoEnabled should be(true)
      }
      "return false if the underlying level is not enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(false)
        logger.isInfoEnabled should be(false)
      }
    }
    "calling with a message" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(true)
        logger.info(message)
        verify(underlying).info(message)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(false)
        logger.info(message)
        verify(underlying, never).info(message)
      }
    }
    "calling with a message and parameters" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(true)
        logger.info(message, args: _*)
        verify(underlying).info(message, args: _*)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(false)
        logger.info(message, args: _*)
        verify(underlying, never).info(message, args: _*)
      }
    }
    "calling with a message and cause" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(true)
        logger.info(message, cause)
        verify(underlying).info(message, cause)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isInfoEnabled).thenReturn(false)
        logger.info(message, cause)
        verify(underlying, never).info(message, cause)
      }
    }
  }

  "warn" when {
    "get level" should {
      "return true if the underlying level is enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(true)
        logger.isWarnEnabled should be(true)
      }
      "return false if the underlying level is not enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(false)
        logger.isWarnEnabled should be(false)
      }
    }
    "calling with a message" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(true)
        logger.warn(message)
        verify(underlying).warn(message)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(false)
        logger.warn(message)
        verify(underlying, never).warn(message)
      }
    }
    "calling with a message and parameters" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(true)
        logger.warn(message, args: _*)
        verify(underlying).warn(message, args: _*)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(false)
        logger.warn(message, args: _*)
        verify(underlying, never).warn(message, args: _*)
      }
    }
    "calling with a message and cause" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(true)
        logger.warn(message, cause)
        verify(underlying).warn(message, cause)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isWarnEnabled).thenReturn(false)
        logger.warn(message, cause)
        verify(underlying, never).warn(message, cause)
      }
    }
  }

  "error" when {
    "get level" should {
      "return true if the underlying level is enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(true)
        logger.isErrorEnabled should be(true)
      }
      "return false if the underlying level is not enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(false)
        logger.isErrorEnabled should be(false)
      }
    }
    "calling with a message" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(true)
        logger.error(message)
        verify(underlying).error(message)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(false)
        logger.error(message)
        verify(underlying, never).error(message)
      }
    }
    "calling with a message and parameters" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(true)
        logger.error(message, args: _*)
        verify(underlying).error(message, args: _*)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(false)
        logger.error(message, args: _*)
        verify(underlying, never).error(message, args: _*)
      }
    }
    "calling with a message and cause" should {
      "call the underlying logger if the level is enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(true)
        logger.error(message, cause)
        verify(underlying).error(message, cause)
      }
      "not call the underlying logger if the level is not enabled" in new WithLogger {
        when(underlying.isErrorEnabled).thenReturn(false)
        logger.error(message, cause)
        verify(underlying, never).error(message, cause)
      }
    }
  }

}
