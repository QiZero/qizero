package qizero.persistence.query

import slick.lifted.Query
import qizero.action.{Action, DBSession}
import qizero.domain.{Page, Pagination}
import qizero.persistence.DAL
import qizero.persistence.mapping.Mapper

trait Queries {

  implicit final class ToFinder[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def find = new Finder(q)
  }

  //  implicit final class Finder[R](find: Find[R]) {
  //    def as[T](implicit mapper: Mapper[R, T]): FindAs[T] = ???
  //    def all: FindAll[R] =
  //    def one: FindOne[R] = ???
  //  }

}

sealed class Finder[R](query: Query[_, R, Seq])
                          (implicit val dal: DAL) {

  def as[E](implicit mapper: Mapper[R, E]): Finder[E] = {
//    query.map(mapper)
    ???
  }
  def first = new FindFirst[R](query)
  def one = new FindOne[R](query)
  def all = new FindAll[R](query)
  def page(pagination: Pagination) = new FindPage[R](query, pagination)
}

sealed trait Find[R] extends DBSession {
  self: Action[_] =>
  val query: Query[_, R, Seq]
}

final class FindFirst[R](val query: Query[_, R, Seq])
                        (implicit val dal: DAL)
  extends Action[R] with Find[R] {

  protected def act(): R = {
    import dal.profile.simple._
    query.first
  }
}

final class FindOne[R](val query: Query[_, R, Seq])
                      (implicit val dal: DAL)
  extends Action[Option[R]] with Find[R] {

  protected def act(): Option[R] = {
    import dal.profile.simple._
    query.firstOption
  }
}

final class FindAll[R](val query: Query[_, R, Seq])
                      (implicit val dal: DAL)
  extends Action[Seq[R]] with Find[R] {

  protected def act(): Seq[R] = {
    import dal.profile.simple._
    query.list
  }
}

final class FindPage[R](val query: Query[_, R, Seq], pagination: Pagination)
                       (implicit val dal: DAL)
  extends Action[Page[R]] with Find[R] {

  import dal.profile.simple._

  protected def act(): Page[R] = {
    val queryPage = query.drop(pagination.offset).take(pagination.pageSize)
    val result = queryPage.list
    val total = query.length.run
    Page(result, pagination, total)
  }
}

