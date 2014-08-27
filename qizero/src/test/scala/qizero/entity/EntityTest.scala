package qizero.entity

import org.scalatest.{Matchers, FunSuite}
import play.api.libs.json.Json

class EntityTest extends FunSuite with Matchers {

//  case class FooId(value:Long) extends TypedId
// case class Foo(name:String,id:FooId) extends Entity[FooId]

//  test("format entity") {
//    val id = FooId(1)
//    val foo = Foo("n", id)
//    val seq = Seq(foo, foo)
//    val opt = Some(foo)
//    val json = Json.toJson(foo)
//    val entity = Json.fromJson[Foo](json)
//    println(json)
//    println(entity)
//
//    val jsonseq = Json.toJson(seq)
//    val entityseq = Json.fromJson[Foo](jsonseq)
//    println(jsonseq)
//    println(entityseq)
//
//    val jsonopt = Json.toJson(opt)
//    val entityopt = Json.fromJson[Foo](jsonopt)
//    println(jsonopt)
//    println(entityopt)
//  }
}
