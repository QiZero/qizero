package qizero.config

import com.typesafe.config.{Config, ConfigFactory}

object Configuration {
  private lazy val underlying = ConfigFactory.load()

  def apply(config: Config): Configuration = new Configuration(config)

  def apply(path: String): Configuration = apply(underlying.getConfig(path))

  def empty(): Configuration = apply(ConfigFactory.empty)

  implicit object ConfigurationReads extends ConfigReads[Configuration] {
    def read(config: Config, path: String): Configuration = apply(config.getConfig(path))
  }

}

final class Configuration private(underlying: Config) {

  def isEmpty: Boolean = underlying.isEmpty

  def hasPath(path: String): Boolean = underlying.hasPath(path)

  def getConfig(path: String): Configuration = get[Configuration](path)

  def get[T](path: String)(implicit reader: ConfigReads[T]): T = reader.read(underlying, path)

  def getAs[T](path: String)(implicit reader: ConfigReads[Option[T]]): Option[T] = reader.read(underlying, path)

  def getOrElse[T](path: String, default: => T)(implicit reader: ConfigReads[Option[T]]): T = getAs[T](path).getOrElse(default)

}