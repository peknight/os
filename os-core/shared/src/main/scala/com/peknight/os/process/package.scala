package com.peknight.os

import cats.data.Ior
import cats.effect.ExitCode
import cats.syntax.eq.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Monad, MonadError}
import com.peknight.error.syntax.applicativeError.asError
import com.peknight.error.{Error, Success}
import com.peknight.os.error.ProcessError
import fs2.Compiler
import fs2.io.process.Process
import fs2.text.{lines, utf8}

package object process:
  def processError[F[_]](process: Process[F])(using Monad[F], Compiler[F, F]): F[Error] =
    for
      exitValue <- process.exitValue
      messages <- process.stderr.through(utf8.decode[F]).through(lines[F]).compile.toList
    yield
      val msgs = messages.filter(_.nonEmpty)
      if ExitCode.Success.code === exitValue && msgs.isEmpty then Success
      else ProcessError(exitValue, msgs)

  def isSuccess[F[_]](process: Process[F])(using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, Boolean]] =
    processError[F](process).asError.map(_.fold(Ior.left, e => Ior.both(e, e.success)))
end process
