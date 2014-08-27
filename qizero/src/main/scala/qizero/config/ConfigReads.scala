package qizero.config

import scala.annotation.implicitNotFound
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.concurrent.duration._
import scala.language.higherKinds
import com.typesafe.config.{Config, ConfigUtil}

@implicitNotFound("No implicit ValueReader defined for ${T}.")
trait ConfigReads[T] {
  def read(config: Config, path: String): T
}

object ConfigReads extends DefaultConfigReads {

  def as[T]: ConfigReads[T] = ??? // TODO Create Macro

  def apply[T](f: (Config, String) => T) = new ConfigReads[T] {
    def read(config: Config, path: String): T = f(config, path)
  }

}

trait DefaultConfigReads {

  implicit object BooleanReads extends ConfigReads[Boolean] {
    def read(config: Config, path: String): Boolean = config.getBoolean(path)
  }

  implicit object IntReads extends ConfigReads[Int] {
    def read(config: Config, path: String): Int = config.getInt(path)
  }

  implicit object ShortReads extends ConfigReads[Short] {
    def read(config: Config, path: String): Short = config.getInt(path).toShort
  }

  implicit object LongReads extends ConfigReads[Long] {
    def read(config: Config, path: String): Long = config.getLong(path)
  }

  implicit object FloatReads extends ConfigReads[Float] {
    def read(config: Config, path: String): Float = config.getDouble(path).toFloat
  }

  implicit object DoubleReads extends ConfigReads[Double] {
    def read(config: Config, path: String): Double = config.getDouble(path)
  }

  implicit object BigDecimalReads extends ConfigReads[BigDecimal] {
    def read(config: Config, path: String): BigDecimal = BigDecimal(config.getString(path))
  }

  implicit object StringReads extends ConfigReads[String] {
    def read(config: Config, path: String): String = config.getString(path)
  }

  implicit object DurationReads extends ConfigReads[Duration] {
    def read(config: Config, path: String): Duration = Duration(config.getDuration(path, MILLISECONDS), MILLISECONDS)
  }

  implicit def OptionReads[T](implicit reads: ConfigReads[T]): ConfigReads[Option[T]] = {
    ConfigReads { (config: Config, path: String) =>
      if (config.hasPath(path)) Some(reads.read(config, path)) else None
    }
  }

  implicit def MapReads[V](implicit reads: ConfigReads[V]): ConfigReads[Map[String, V]] = {
    ConfigReads { (config: Config, path: String) =>
      val relativeConfig = config.getConfig(path)
      relativeConfig.root.entrySet.asScala.map { entry =>
        val key = entry.getKey
        key -> reads.read(relativeConfig, ConfigUtil.quoteString(key))
      }.toMap
    }
  }

  private val DummyPathValue = "dummyPathValue"
  implicit def TranversableReads[C[_], A](implicit reads: ConfigReads[A], bf: CanBuildFrom[C[_], A, C[A]]): ConfigReads[C[A]] = {
    ConfigReads { (config: Config, path: String) =>
      val list = config.getList(path).asScala
      val builder = bf()
      builder.sizeHint(list.size)
      list.foreach { entry =>
        val entryConfig = entry.atPath(DummyPathValue)
        builder += reads.read(entryConfig, DummyPathValue)
      }
      builder.result()
    }
  }
}