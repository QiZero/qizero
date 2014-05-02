package qizero.action

import java.util.concurrent.ConcurrentHashMap
import scala.util.DynamicVariable

trait Cache extends Invoker {
  _: Action[_] =>

  final def isResponseFromCache: Boolean = false

  protected def cacheKey: String

  abstract override protected def invoke(): Response = {
    Cache(actionKey) match {
      case Some(value) => value
      case None => super.invoke()
    }
  }
}

object Cache {
  private val caches = new ConcurrentHashMap[ActionKey, DynamicVariable[Option[Nothing]]]

  def apply(key: ActionKey): Option[Nothing] = {
    Option(caches.get(key)) match {
      case Some(dyn) => dyn.value
      case None =>
        val dyn = new DynamicVariable[Option[Nothing]](None)
        caches.putIfAbsent(key, dyn)
        None
    }
  }
}
