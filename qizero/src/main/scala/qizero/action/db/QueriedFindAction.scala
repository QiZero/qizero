package qizero.action.db

import scala.slick.lifted.Query
import qizero.model.{Slice, Pagination, Page}
import qizero.persistence.DAL

sealed trait QueriedFindAction[R] extends QueriedAction[R] {
  self: DBAction[_] =>

  lazy val statement: String = {
    import dal.profile.simple._
    query.selectStatement
  }
}

final class QueriedFindFirst[R](val query: Query[_, R, Seq])
                        (implicit val dal: DAL)
  extends DBAction[R] with QueriedFindAction[R] {

  import dal.profile.simple._

  protected def act(): R = query.first
}

final class QueriedFindOne[R](val query: Query[_, R, Seq])
                      (implicit val dal: DAL)
  extends DBAction[Option[R]] with QueriedFindAction[R] {

  import dal.profile.simple._

  protected def act(): Option[R] = query.firstOption
}

final class QueriedFindAll[R](val query: Query[_, R, Seq])
                      (implicit val dal: DAL)
  extends DBAction[List[R]] with QueriedFindAction[R] {

  import dal.profile.simple._

  protected def act(): List[R] = query.list
}

final class QueriedFindPage[R](val query: Query[_, R, Seq], val pagination:Pagination)
                       (implicit val dal: DAL)
  extends DBAction[Page[R]] with QueriedFindAction[R] {

  import dal.profile.simple._

  protected def act(): Page[R] = {
    val queryPage = query.drop(pagination.offset).take(pagination.pageSize)
    val result = queryPage.list
    val total = query.length.run
    new Page(result, pagination, total)
  }
}

final class QueriedFindSlice[R](val query: Query[_, R, Seq], val pagination:Pagination)
                              (implicit val dal: DAL)
  extends DBAction[Slice[R]] with QueriedFindAction[R] {

  import dal.profile.simple._

  protected def act(): Slice[R] = {
    val queryPage = query.drop(pagination.offset).take(pagination.pageSize)
    val result = queryPage.list
    new Slice(result, pagination)
  }
}

final class QueriedFindCount[R](val query: Query[_, R, Seq])
                        (implicit val dal: DAL)
  extends DBAction[Int] with QueriedFindAction[R] {

  import dal.profile.simple._

  lazy private val lengthQuery = query.length

  override lazy val statement = lengthQuery.selectStatement

  protected def act(): Int = lengthQuery.run
}

final class QueriedFindExists[R](val query: Query[_, R, Seq])
                        (implicit val dal: DAL)
  extends DBAction[Boolean] with QueriedFindAction[R] {

  import dal.profile.simple._

  lazy private val existsQuery = query.exists

  override lazy val statement = existsQuery.selectStatement

  protected def act(): Boolean = existsQuery.run
}