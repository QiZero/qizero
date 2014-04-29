package qizero.logging

trait Loggable {
  protected final lazy val log: Logger = Logger(getClass)
}
