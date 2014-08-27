package qizero.persistence

import java.util.concurrent.ConcurrentHashMap
import scala.slick.driver._
import qizero.config.Configuration

object DB {
  private val config = Configuration("db")
  private val cachedDatabases = new ConcurrentHashMap[String, Database]()

  private val driverByName = Map(
    "org.apache.derby.jdbc.EmbeddedDriver" -> DerbyDriver,
    "org.h2.Driver" -> H2Driver,
    "org.hsqldb.jdbcDriver" -> HsqldbDriver,
    "com.mysql.jdbc.Driver" -> MySQLDriver,
    "org.postgresql.Driver" -> PostgresDriver,
    "org.sqlite.JDBC" -> SQLiteDriver
  )

  def getProfile(name: String): Profile = {
    val driver = config.get[String](s"$name.driver")
    val jdbcDriver = driverByName.get(driver)
    jdbcDriver.getOrElse(throw new IllegalArgumentException("database.invalid_driver"))
  }

  def getDatabase(name: String)(implicit profile: Profile): Database = {
    val cached = Option(cachedDatabases.get(name))
    cached.getOrElse {
      val db = createDatabase(name)
      cachedDatabases.putIfAbsent(name, db)
      db
    }
  }

  private def createDatabase(name: String)(implicit profile: Profile): Database = {
    profile.simple.Database.forURL(
      driver = config.get[String](s"$name.driver"),
      url = config.get[String](s"$name.url"),
      user = config.getAs[String](s"$name.user").orNull,
      password = config.getAs[String](s"$name.password").orNull
    )
  }
}