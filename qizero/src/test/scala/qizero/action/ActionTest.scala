package qizero.action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class ActionTest extends WordSpec with Matchers with ScalaFutures {

  case class Echo(echo: Int) extends Action[Int] {
    protected def act() = echo
  }

  case class EchoFail(echo: Int) extends Action[Int] {
    protected def act() = failure("echo.failed")
  }

  "Action" should {
    "execute sync" in {
      val action = Echo(Random.nextInt)
      val result = action.run
      result should be(action.echo)
    }

    "execute async" in {
      val action = Echo(Random.nextInt)
      val asyncResult = action.async
      whenReady(asyncResult) { r =>
        r should be(action.echo)
      }
    }

    "map" in {
      val action = Echo(Random.nextInt)
      val mapAction = action.map(r => r.toString)
      val result = mapAction.run
      result should be(action.echo.toString)
    }

    "flatMap" in {
      val action = Echo(Random.nextInt)
      val flatMapAction = action.flatMap(r => Echo(r + 1))
      val result = flatMapAction.run
      result should be(action.echo + 1)
    }

    "throw a expection" in {
      intercept[Exception] {
        EchoFail(Random.nextInt).run
      }
    }
  }

}
