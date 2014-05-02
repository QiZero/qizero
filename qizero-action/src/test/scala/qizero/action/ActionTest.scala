package qizero.action

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class ActionTest extends WordSpec with Matchers with ScalaFutures {

  case class Echo(echo: Int) extends Action[Int] {
    protected def act() = echo
  }

  case class EchoFail(echo: Int) extends Action[Int] {
    protected def act() = failure("echo.failed")
  }

  "Action" should {
    "execute sync" in {
      val echo = Random.nextInt
      val result = Echo(echo).run
      result should be(echo)
    }
    "execute async" in {
      val echo = Random.nextInt
      val asyncResult = Echo(echo).ask
      whenReady(asyncResult) { r =>
        r should be(echo)
      }
    }
    "map" in {
      val echo = Random.nextInt
      val mapAction = Echo(echo).map(r => r.toString)
      val result = mapAction.run
      result should be(echo.toString)
    }

    "throw a expection" in {
      intercept[Exception] {
        EchoFail(Random.nextInt).run
      }
    }
  }

}
