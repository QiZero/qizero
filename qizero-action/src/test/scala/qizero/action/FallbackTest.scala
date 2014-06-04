package qizero.action

import org.scalatest.{Matchers, WordSpec}
import scala.util.Random

class FallbackTest extends WordSpec with Matchers {

  case class Echo(echo: Int) extends Action[Int] with Fallback {
    protected def act() = failure("fallback")
    protected def fallback() = echo + 1
  }

  "Fallback" should {
    "return a fallback result" in {
      val action = Echo(Random.nextInt)
      val result = action.run
      result should be(action.echo + 1)
      action.isResultFromFallback should be(true)
    }

  }

}
