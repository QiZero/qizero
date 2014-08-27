package qizero.persistence.query

import scala.language.higherKinds
import scala.slick.lifted.{Query, RunnableCompiled}
import qizero.action.db._
import qizero.model._
import qizero.persistence.DAL
import qizero.persistence.mapping.Mapper

trait Finder[R] {
  def first: DBAction[R]
  def one: DBAction[Option[R]]
  def list: DBAction[List[R]]
  def page(pagination: Pagination): DBAction[Page[R]]
  def slice(pagination: Pagination): DBAction[Slice[R]]
  def count: DBAction[Int]
  def exists: DBAction[Boolean]

  def as[E](implicit mapper: Mapper[R, E]): Finder[E] = new MappedFinder[R, E](this, mapper)
}

private final class QueriedFinder[R](query: Query[_, R, Seq])(implicit dal: DAL) extends Finder[R] {
  def first = new QueriedFindFirst(query)
  def one = new QueriedFindOne(query)
  def list = new QueriedFindAll(query)
  def page(pagination: Pagination) = new QueriedFindPage(query, pagination)
  def slice(pagination: Pagination) = new QueriedFindSlice(query, pagination)
  override def count = new QueriedFindCount[R](query)
  override def exists = new QueriedFindExists[R](query)
}

private final class CompiledFinder[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])(implicit dal: DAL) extends Finder[R] {
  def first = new CompiledFindFirst(compiled)
  def one = new CompiledFindOne(compiled)
  def list = new CompiledFindAll(compiled)
  def page(pagination: Pagination) = new CompiledFindPage(compiled, pagination)
  def slice(pagination: Pagination) = new CompiledFindSlice(compiled, pagination)
  def count = new CompiledFindCount(compiled)
  def exists = new CompiledFindExists(compiled)
}

private final class MappedFinder[R, E](finder: Finder[R], mapper: Mapper[R, E]) extends Finder[E] {
  def first = new MapDBAction(finder.first, mapper)
  def one = new MapDBAction(finder.one, mapper)
  def list = new MapDBAction(finder.list, mapper)
  def page(pagination: Pagination) = new MapDBAction(finder.page(pagination), mapper)
  def slice(pagination: Pagination) = new MapDBAction(finder.slice(pagination), mapper)
  def count = finder.count
  def exists = finder.exists
}