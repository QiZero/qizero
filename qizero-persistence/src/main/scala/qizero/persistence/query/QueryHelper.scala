package qizero.persistence.query

trait QueryHelper
  extends IdQuery
  with FindQuery
  with InsertQuery
  with MappingQuery

object QueryHelper extends QueryHelper