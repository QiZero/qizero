package qizero.event

trait Publisher {

  final def publish(event: Event): Unit = {
    EventBus().publish(event)
  }

}
