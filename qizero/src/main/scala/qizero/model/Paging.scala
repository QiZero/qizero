package qizero.model

import play.api.libs.json._
import scala.language.implicitConversions

final case class Pagination(page: Int, size: Int) {
  require(page > 0, "Page index must be greater than zero!")
  require(size > 0, "Size must be equal or greater than zero!")

  val offset: Int = (page - 1) * size

  def next: Pagination = Pagination(page + 1, size)

  def previous: Pagination = {
    if (page <= 1) this
    else Pagination(page - 1, size)
  }

  def first: Pagination = Pagination(1, size)
}

trait Paging[T] {
  def content: Seq[T]
}

trait Chuck[T] extends Paging[T] {

  def pagination: Pagination

  def page: Int = pagination.page

  def size: Int = pagination.size

  def count: Int = content.length

  def hasContent: Boolean = content.nonEmpty

  def isFirst: Boolean = !hasPrevious

  def isLast: Boolean = !hasNext

  def hasNext: Boolean

  def hasPrevious: Boolean = page > 1

  def next: Pagination = pagination.next

  def previous: Pagination = pagination.previous

}

final case class Slice[T](
                           content: Seq[T],
                           pagination: Pagination
                           ) extends Chuck[T] {

  lazy val hasNext: Boolean = content.isEmpty

  def map[B](f: T => B): Slice[B] = new Slice(content.map(f), pagination)

}

object Slice {

  implicit def SliceWrites[T: Writes]: Writes[Slice[T]] = Writes { p: Slice[T] =>
    Json.obj(
      "data" -> p.content,
      "paging" -> Json.obj(
        "page" -> p.page,
        "size" -> p.size,
        "count" -> p.count,
        "hasNext" -> p.hasNext,
        "hasPrevious" -> p.hasPrevious
      )
    )
  }

}

final case class Page[T](
                          content: Seq[T],
                          pagination: Pagination,
                          total: Int
                          ) extends Chuck[T] {
  require(total >= 0, "Total must not be less than zero!")

  val totalPages: Int = Math.ceil(total.toDouble / size.toDouble).toInt

  def hasNext: Boolean = page < totalPages

  def map[B](f: T => B): Page[B] = new Page(content.map(f), pagination, total)
}

object Page {

  implicit def PageWrites[T: Writes]: Writes[Page[T]] = Writes { p: Page[T] =>
    Json.obj(
      "data" -> p.content,
      "paging" -> Json.obj(
        "page" -> p.page,
        "size" -> p.size,
        "count" -> p.count,
        "total" -> p.total,
        "hasNext" -> p.hasNext,
        "hasPrevious" -> p.hasPrevious
      )
    )
  }
}


trait CursorA[T] {
  def size:Int
  def after:Option[T]
  def before:Option[T]
}
final case class Cursor(size: Int, after: Option[String] = None, before: Option[String] = None) {
  require(size > 0, "Size must be equal or greater than zero!")
}

object Cursor {

  implicit class UpdatedCursor(cursor: Cursor) {
    def update[T](content: Seq[T])(implicit write: T => String): Cursor = {
      val after = content.headOption.map(write).orElse(cursor.after).orElse(cursor.before)
      val before = content.lastOption.map(write).orElse(cursor.before).orElse(cursor.after)
      cursor.copy(after = after, before = before)
    }
  }

}

final case class Line[T](
                          content: Seq[T],
                          cursor: Cursor
                          ) extends Paging[T] {

  def size: Int = cursor.size

  def count: Int = content.length
}

object Line {

  implicit def LineWrites[T: Writes]: Writes[Line[T]] = Writes { p: Line[T] =>
    Json.obj(
      "data" -> p.content,
      "paging" -> Json.obj(
        "after" -> p.cursor.after,
        "before" -> p.cursor.before,
        "size" -> p.size,
        "count" -> p.count
      )
    )
  }

}
