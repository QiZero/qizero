package qizero.persistence

trait HasProfile {
  val profile: Profile
}

trait HasDB {
  val db: Database
}

abstract class HasDAL[D <: DAL](val dal: D)

trait DAL extends HasProfile with HasDB {
  implicit val _dal: this.type = this

  object session {
    implicit def dynamicSession: Session = profile.simple.Database.dynamicSession
  }

}

abstract class NamedDAL(val name:String) extends DAL {
  val profile: Profile = DB.getProfile(name)
  val db: Database = DB.getDatabase(name)(profile)
}