package qizero.model

import play.api.libs.json._
import scala.language.implicitConversions

final case class Pagination(pageNumber: Int, pageSize: Int) {
  require(pageNumber > 0, "Page index must be greater than zero!")
  require(pageSize > 0, "Size must be equal or greater than zero!")

  val offset: Int = (pageNumber - 1) * pageSize

  def next: Pagination = Pagination(pageNumber + 1, pageSize)

  def previous: Pagination = {
    if (pageNumber <= 1) this
    else Pagination(pageNumber - 1, pageSize)
  }

  def first: Pagination = Pagination(1, pageSize)
}

trait Chuck[T] {

  def pagination: Pagination

  def content: Seq[T]

  def number: Int = pagination.pageNumber

  def size: Int = pagination.pageSize

  def count: Int = content.length

  def hasContent: Boolean = content.nonEmpty

  def isFirst: Boolean = !hasPrevious

  def isLast: Boolean = !hasNext

  def hasNext: Boolean

  def hasPrevious: Boolean = number > 1

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
        "number" -> p.number,
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

  def hasNext: Boolean = number < totalPages

  def map[B](f: T => B): Page[B] = new Page(content.map(f), pagination, total)
}

object Page {

  implicit def PageWrites[T: Writes]: Writes[Page[T]] = Writes { p: Page[T] =>
    Json.obj(
      "data" -> p.content,
      "paging" -> Json.obj(
        "number" -> p.number,
        "size" -> p.size,
        "count" -> p.count,
        "total" -> p.total,
        "hasNext" -> p.hasNext,
        "hasPrevious" -> p.hasPrevious
      )
    )
  }
}

final case class Cursor(size: Int, before: Option[String] = None, after: Option[String] = None) {
  require(size > 0, "Size must be equal or greater than zero!")
}

object Cursor {

  implicit class UpdatedCursor(cursor: Cursor) {
    def update[T](content: Seq[T])(implicit write: T => String): Cursor = {
      val before = content.headOption.map(write).orElse(cursor.before).orElse(cursor.after)
      val after = content.lastOption.map(write).orElse(cursor.after).orElse(cursor.before)
      cursor.copy(before = before, after = after)
    }
  }

}

final case class Line[T](
                          content: Seq[T],
                          cursor: Cursor
                          ) {

  def size: Int = cursor.size

  def count: Int = content.length
}

object Line {

  implicit def CursoredChuckWrites[T: Writes]: Writes[Line[T]] = Writes { p: Line[T] =>
    Json.obj(
      "data" -> p.content,
      "paging" -> Json.obj(
        "cursor" -> Json.obj(
          "before" -> p.cursor.before,
          "after" -> p.cursor.after
        ),
        "size" -> p.size,
        "count" -> p.count
      )
    )
  }

}