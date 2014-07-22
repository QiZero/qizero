import qizero.persistence.mapping.Mapper

object Macro extends App {

  case class Row(name: String, count: Int, id: Option[Long])

  case class Entity(name: String, id: Long)

  val mapper = Mapper.create[Row, Entity]

  val row = Row("test", 1, Some(10))
  val tuple = (row, true)
  val entity = mapper(row)

  println(entity)

}
