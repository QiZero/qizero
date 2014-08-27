package qizero.config

abstract class Configurable(path: String) {
  @transient
  protected final lazy val config = Configuration(path)
}
