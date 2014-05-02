package qizero.message

trait Message

trait Reply[R] {
  _: Message =>
}

trait NoReply extends Reply[Unit] {
  _: Message =>
}

trait OptionReply[R] extends Reply[Option[R]] {
  _: Message =>
}
