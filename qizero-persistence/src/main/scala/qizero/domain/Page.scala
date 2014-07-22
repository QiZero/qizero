package qizero.domain

final case class Page[+T](content: Seq[T], pagination: Pagination, total: Int) {
  require(total >= 0, "Total must not be less than zero!")

  def pageNumber: Int = pagination.pageNumber

  def pageSize: Int = pagination.pageSize

  def totalPages: Int = Math.ceil(total / pageSize).toInt

  def totalElements: Int = total

  def isFirstPage: Boolean = !hasPreviousPage

  def isLastPage: Boolean = !hasNextPage

  def hasPreviousPage: Boolean = pageNumber > 0

  def hasNextPage: Boolean = (pageNumber + 1) * pageSize < total

  def next: Pagination = if (hasNextPage) pagination.next else pagination

  def previous: Pagination = if (hasPreviousPage) pagination.previous else pagination

  def first: Pagination = pagination.first

  def last: Pagination = Pagination(totalPages, pageSize)

  def current: Pagination = pagination

  def length: Int = content.length

  def map[B](f: T => B): Page[B] = new Page(content.map(f), pagination, total)

}

final case class Pagination(pageNumber: Int, pageSize: Int) {
  require(pageNumber >= 0, "Page index must not be less than zero!")
  require(pageSize > 0, "Size must not be less than zero!")
  require(pageSize <= 50, "Size must be less than fifty!")

  def offset: Int = pageNumber * pageSize

  def next: Pagination = Pagination(pageNumber + 1, pageSize)

  def previous: Pagination = {
    if (pageNumber > 0) Pagination(pageNumber - 1, pageSize)
    else this
  }

  def first: Pagination = Pagination(0, pageSize)
}

object Pagination {
  val default = Pagination(0, 25)
}
