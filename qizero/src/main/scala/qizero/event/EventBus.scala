package qizero.event

import akka.event.{ActorEventBus, LookupClassification}

class EventBus extends ActorEventBus with LookupClassification {
  type Classifier = Class[_]
  type Event = qizero.event.Event

  protected def mapSize(): Int = 16

  protected def publish(event: Event, subscriber: Subscriber): Unit = {
    subscriber ! event
  }

  protected def classify(event: Event): Classifier = {
    event.getClass
  }

}

object EventBus {
  private lazy val default = new EventBus

  @volatile
  private var cache = Map.empty[String, EventBus]

  def apply() = default

  def apply(name: String) = {
    cache.getOrElse(name, {
      val eventBus = new EventBus
      cache += (name -> eventBus)
      eventBus
    })
  }
}