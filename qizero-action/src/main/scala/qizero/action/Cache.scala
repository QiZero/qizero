package qizero.action

import java.util.concurrent.ConcurrentHashMap
import scala.util.DynamicVariable

trait Cache extends Invoker {
  _: Action[_] =>

  final def isResponseFromCache: Boolean = false

  protected def cacheKey: String

  abstract override protected def invoke(): Result = {
    // Cache(actionKey) match {
    //   case Some(value) => value
    //   case None => super.invoke()
    // }
    ???
  }
}

object Cache {
  private val caches = new ConcurrentHashMap[ActionKey, DynamicVariable[Option[Any]]]

  def apply(key: ActionKey): Option[Any] = {
    Option(caches.get(key)) match {
      case Some(dyn) => dyn.value
      case None =>
        val dyn = new DynamicVariable[Option[Any]](None)
        caches.putIfAbsent(key, dyn)
        None
    }
  }
}
