package qizero.logging

trait Loggable {
  @transient
  protected final lazy val log: Logger = Logger(getClass)
}
