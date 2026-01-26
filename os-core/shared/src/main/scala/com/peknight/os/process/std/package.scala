package com.peknight.os.process

import cats.data.Ior
import cats.effect.{Async, ExitCode}
import cats.syntax.applicative.*
import cats.syntax.eq.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.error.syntax.applicativeError.asError
import com.peknight.error.{Error, Success}
import com.peknight.os.error.ProcessError
import fs2.io.process.Process
import fs2.io.{stderr, stdout}
import fs2.text.utf8
import fs2.{Compiler, Stream}

package object std:
  def processError[F[_]: Async](process: Process[F]): F[(Error, Stream[F, Byte])] =
    for
      exitValue <- process.exitValue
      messages <- process.stderr.observe(stderr[F]).through(utf8.decode[F]).through(fs2.text.lines[F]).compile.toList
    yield
      val msgs = messages.filter(_.nonEmpty)
      val stream = process.stdout.observe(stdout[F])
      val error =
        if ExitCode.Success.code === exitValue && msgs.isEmpty then Success
        else ProcessError(exitValue, msgs)
      (error, stream)

  def isSuccess[F[_]: Async](process: Process[F]): F[Ior[Error, Boolean]] =
    processError[F](process).asError.flatMap(_.fold(
      error => Ior.left(error).pure[F],
      (error, stream) => stream.compile.drain.map(_ => Ior.both(error, error.success))
    ))

end std
