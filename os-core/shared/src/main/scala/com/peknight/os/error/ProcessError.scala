package com.peknight.os.error

import com.peknight.error.Error

case class ProcessError(exitValue: Int, override val messages: List[String]) extends Error:
  override val success: Boolean = exitValue == 0
  override def lowPriorityMessage: Option[String] =
    val msg = messages.mkString("\n")
    if msg.isBlank then Some(s"$exitValue")
    else Some(s"$exitValue: ${messages.mkString("\n")}")
end ProcessError
