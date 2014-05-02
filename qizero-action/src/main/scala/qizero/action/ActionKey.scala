package qizero.action

import java.util.concurrent.ConcurrentHashMap

class ActionKey private(val name: String)

object ActionKey {
  private val keys = new ConcurrentHashMap[String, ActionKey]()
  private val names = new ConcurrentHashMap[Class[_], String]()

  def apply(name: String): ActionKey = {
    Option(keys.get(name)) match {
      case Some(key) => key
      case None =>
        val key = new ActionKey(name)
        keys.putIfAbsent(name, key)
    }
  }

  def apply(clazz: Class[_]): ActionKey = {
    apply(getNameFromClass(clazz))
  }

  private def getNameFromClass(clazz: Class[_]): String = {
    Option(names.get(clazz)) match {
      case Some(name) => name
      case None =>
        names.putIfAbsent(clazz, clazz.getSimpleName)
    }
  }
}
