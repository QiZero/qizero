package qizero

import slick.driver.{JdbcDriver, JdbcProfile}
import slick.jdbc.JdbcBackend

package object persistence {
  type Session = JdbcBackend#Session
  type Database = JdbcBackend#Database
  type Profile = JdbcProfile
  type Driver = JdbcDriver
}