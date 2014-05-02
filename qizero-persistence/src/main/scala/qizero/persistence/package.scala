package qizero

import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

package object persistence {
  type Session = JdbcBackend#Session
  type Database = JdbcBackend#Database
  type Profile = JdbcProfile
  type Driver = JdbcDriver
}