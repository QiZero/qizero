package qizero.action.db

import qizero.persistence._
import slick.ast.BaseTypedType
import slick.lifted._

trait InsertAction[R] extends QueriedAction[R] {
  self: DBAction[_] =>

  lazy val statement: String = {
    import dal.profile.api._
    query.insertStatement
  }
}

final class Insert[R](val query: Query[_, R, Seq], row: R)
                     (implicit val dal: DAL)
  extends DBAction[R] with InsertAction[R] {

  import dal.profile.simple._

  protected def act(): R = {
    println("Insert")
    query.insert(row)
    row
  }
}

final class InsertReturningId[R <: AutoIncId[ID], ID](val query: Query[_ <: HasAutoIncId[ID], R, Seq], row: R)
                                                     (implicit val dal: DAL, typedType: BaseTypedType[ID])
  extends DBAction[R] with InsertAction[R] {

  import dal.profile.simple._

  protected def act(): R = {
    println("InsertId")
    val id = query.returning(query.map(_.id)).insert(row)
    copyId(row, id)
  }

  private def copyId(row: R, id: ID): R = {
    println(s"ROW $row $id")
    row.withId(id).asInstanceOf[R]
  }
}