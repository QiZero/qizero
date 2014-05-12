package qizero.config

class Config(path: String) {
  @transient
  protected final lazy val config = Configuration(path)
}
