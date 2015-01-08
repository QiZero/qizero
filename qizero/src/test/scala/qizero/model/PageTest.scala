package qizero.model

import org.scalatest.{Matchers, WordSpec}
import qizero.annotation.json
import qizero.entity.{Entity, Id, TypedId}

class PageTest extends WordSpec with Matchers {

  @json case class FooId(value: Long) extends TypedId

  @json case class Foo(id: FooId) extends Entity with Id[FooId]

  implicit val writer = CursorWriter[Foo](_.id.value.toString)

  "Line" when {
    "Line" should {

      "3 content" in {
        val cursor = Cursor(5, Some("1"), None)
        val content = Seq(Foo(1), Foo(2), Foo(3))
        val line = new Line(content, cursor)
        line.size shouldBe cursor.size
        line.count shouldBe content.length
        line.before shouldBe Some("1")
        line.after shouldBe Some("3")
      }

      "empty content with before cursor" in {
        val cursor = Cursor(5, Some("1"), None)
        val content = Seq.empty[Foo]
        val line = new Line(content, cursor)
        line.size shouldBe cursor.size
        line.count shouldBe content.length
        line.before shouldBe Some("1")
        line.after shouldBe Some("1")
      }

      "empty content with after cursor" in {
        val cursor = Cursor(5, None, Some("3"))
        val content = Seq.empty[Foo]
        val line = new Line(content, cursor)
        line.size shouldBe cursor.size
        line.count shouldBe content.length
        line.before shouldBe Some("3")
        line.after shouldBe Some("3")
      }

      "two content" in {
        val cursor = Cursor(5, None, Some("1"))
        val content = Seq(Foo(1), Foo(2))
        val line = new Line(content, cursor)
        line.size shouldBe cursor.size
        line.count shouldBe content.length
        line.before shouldBe Some("1")
        line.after shouldBe Some("2")
      }

      "one content" in {
        val cursor = Cursor(5, None, Some("1"))
        val content = Seq(Foo(5))
        val line = new Line(content, cursor)
        line.size shouldBe cursor.size
        line.count shouldBe content.length
        line.before shouldBe Some("5")
        line.after shouldBe Some("5")
      }
    }
  }

}
