package qizero.persistence

import java.util.concurrent.ConcurrentHashMap
import qizero.config.Config
import scala.slick.driver._

object DB extends Config("db") {

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
    val driver = config.getString(s"$name.driver")
    val jdbcDriver = driverByName.get(driver)
    jdbcDriver.getOrElse(throw new IllegalArgumentException("database.invalid_driver"))
  }

  def getDB(name: String)(implicit profile: Profile): Database = {
    val cached = Option(cachedDatabases.get(name))
    cached.getOrElse {
      val db = createDB(name)
      cachedDatabases.put(name, db)
      db
    }
  }

  private def createDB(name: String)(implicit profile: Profile): Database = {
    profile.simple.Database.forURL(
      driver = config.getString(s"$name.driver"),
      url = config.getString(s"$name.url"),
      user = config.getString(s"$name.user"),
      password = config.getString(s"$name.password")
    )
  }
}