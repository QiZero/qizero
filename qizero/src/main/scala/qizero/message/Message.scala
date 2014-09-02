package qizero.message

trait Message

trait Reply[R] {
  _: Message =>
}
