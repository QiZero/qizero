package qizero.service

import akka.actor._
import qizero.message.Message

trait ServiceSystem {
  def system: ActorSystem

  final protected def service[M <: Message](props: Props, name: String): ServiceRef[M] = {
    val ref = system.actorOf(props, name)
    ServiceRef[M](ref)
  }
}