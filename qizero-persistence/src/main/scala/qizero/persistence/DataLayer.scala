package qizero.persistence

trait HasProfile {
  val profile: Profile
}

trait HasDB {
  val db: Database
}

class DAL(val name: String) extends HasProfile with HasDB {
  implicit val _dal: this.type = this
  val profile: Profile = DB.getProfile(name)
  val db: Database = DB.getDatabase(name)(profile)

  object session {
    def dynamicSession: Session = profile.simple.Database.dynamicSession
  }

}