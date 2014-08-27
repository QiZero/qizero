package qizero.logging

trait Logging {
  @transient
  protected final lazy val log = Logger(getClass)
}
