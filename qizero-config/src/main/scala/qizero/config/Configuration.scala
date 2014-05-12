package qizero.config

import com.typesafe.config.{ConfigFactory, Config => TConfig}
import scala.collection.JavaConverters._
import scala.concurrent.duration._

object Configuration {
  private lazy val underlying = ConfigFactory.load()

  def apply(config: TConfig): Configuration = new Configuration(config)

  def apply(path: String, config: TConfig = underlying): Configuration = apply(
    config.getConfig(path)
  )

  def empty(): Configuration = apply(ConfigFactory.empty)
}

final class Configuration private(underlying: TConfig) {

  def isEmpty(): Boolean = underlying.isEmpty

  def hasPath(path: String): Boolean = underlying.hasPath(path)

  // Config
  def getConfig(path: String): Configuration = Configuration(path, underlying)

  // String
  def getString(path: String): String = underlying.getString(path)

  def getStringOption(path: String): Option[String] = getOption(path, getString(path))

  def getStringOrElse(path: String, default: => String): String = getOrElse(path, getString(path), default)

  def getStrings(path: String): List[String] = getList(path, underlying.getStringList(path))

  // Boolean
  def getBoolean(path: String): Boolean = underlying.getBoolean(path)

  def getBooleanOption(path: String): Option[Boolean] = getOption(path, getBoolean(path))

  def getBooleanOrElse(path: String, default: => Boolean): Boolean = getOrElse(path, getBoolean(path), default)

  def getBooleans(path: String): List[Boolean] = getList(path, underlying.getBooleanList(path)).map(Boolean.unbox)

  // Int
  def getInt(path: String): Int = underlying.getInt(path)

  def getIntOption(path: String): Option[Int] = getOption(path, getInt(path))

  def getIntOrElse(path: String, default: => Int): Int = getOrElse(path, getInt(path), default)

  def getInts(path: String): List[Int] = getList(path, underlying.getIntList(path)).map(Int.unbox)

  // Long
  def getLong(path: String): Long = underlying.getLong(path)

  def getLongOption(path: String): Option[Long] = getOption(path, getLong(path))

  def getLongOrElse(path: String, default: => Long): Long = getOrElse(path, getLong(path), default)

  def getLongs(path: String): List[Long] = getList(path, underlying.getLongList(path)).map(Long.unbox)

  // Double
  def getDouble(path: String): Double = underlying.getDouble(path)

  def getDoubleOption(path: String): Option[Double] = getOption(path, getDouble(path))

  def getDoubleOrElse(path: String, default: => Double): Double = getOrElse(path, getDouble(path), default)

  def getDoubles(path: String): List[Double] = getList(path, underlying.getDoubleList(path)).map(Double.unbox)

  // Duration
  def getDuration(path: String, unit: TimeUnit = MILLISECONDS): Duration = Duration(underlying.getDuration(path, unit), unit)

  def getDurationOption(path: String, unit: TimeUnit = MILLISECONDS): Option[Duration] = getOption(path, getDuration(path, unit))

  def getDurationOrElse(path: String, default: => Duration, unit: TimeUnit = MILLISECONDS): Duration = getOrElse(path, getDuration(path, unit), default)

  def getDurationList(path: String, unit: TimeUnit = MILLISECONDS): List[Duration] = getList(path, underlying.getDurationList(path, unit)).map(Duration(_, unit))

  // Utils
  private def getOrElse[T](path: String, get: => T, default: => T) = if (hasPath(path)) get else default

  private def getOption[T](path: String, get: => T) = if (hasPath(path)) Some(get) else None

  private def getList[T](path: String, get: => java.util.List[T]): List[T] = get.asScala.toList
}