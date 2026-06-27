package com.peknight.os.error

import cats.effect.ExitCode
import cats.syntax.eq.*
import com.peknight.error.{Error, Success}

case class ProcessError(exitValue: Int, override val messages: List[String]) extends Error:
  override val success: Boolean = exitValue == 0
  override def lowPriorityMessage: Option[String] =
    val msg = messages.mkString("\n")
    if msg.isBlank then Some(s"$exitValue")
    else Some(s"$exitValue: ${messages.mkString("\n")}")
end ProcessError
object ProcessError:
  def from(exitValue: Int, messages: List[String]): Error =
    val msgs = messages.filter(_.nonEmpty)
    if ExitCode.Success.code === exitValue && msgs.isEmpty then Success
    else ProcessError(exitValue, msgs)
end ProcessError
