package qizero.action

import qizero.message.{Reply, Message}

abstract class ReplyAction[R](message: Message with Reply[R]) extends Action[R]
