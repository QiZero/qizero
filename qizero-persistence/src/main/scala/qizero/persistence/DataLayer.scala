package qizero.persistence

trait HasProfile {
  val profile: Profile
}

trait HasDB {
  val db: Database
}

trait Schema extends HasProfile with HasDB {
  def name: String
  implicit val profile: Profile = DB.getProfile(name)
  implicit val db: Database = DB.getDatabase(name)

  object session {
    def dynamicSession: Session = profile.simple.Database.dynamicSession
  }

}

class DAL(val name: String) extends Schema
