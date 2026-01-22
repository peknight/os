package com.peknight.os

import cats.data.{Ior, IorT}
import cats.effect.ExitCode
import cats.syntax.eq.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Monad, MonadError}
import com.peknight.error.syntax.applicativeError.{asError, asIT}
import com.peknight.error.{Error, Success}
import com.peknight.os.error.ProcessError
import fs2.io.process.Process
import fs2.text.utf8
import fs2.{Compiler, Pipe, Stream}

package object process:
  def processError[F[_]](process: Process[F])(using Monad[F], Compiler[F, F]): F[Error] =
    for
      exitValue <- process.exitValue
      messages <- process.stderr.through(utf8.decode[F]).through(fs2.text.lines[F]).compile.toList
    yield
      val msgs = messages.filter(_.nonEmpty)
      if ExitCode.Success.code === exitValue && msgs.isEmpty then Success
      else ProcessError(exitValue, msgs)

  def isSuccess[F[_]](process: Process[F])(using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, Boolean]] =
    processError[F](process).asError.map(_.fold(Ior.left, e => Ior.both(e, e.success)))

  def stdout[F[_]](process: Process[F])(using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, Stream[F, Byte]]] =
    processError[F](process).asError.map(_.fold(Ior.left, e => if e.success then Ior.both(e, process.stdout) else Ior.left(e)))

  def lines[F[_]](process: Process[F], decode: Pipe[F, Byte, String] = utf8.decode[F])
                 (using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, Stream[F, String]]] =
    stdout[F](process).map(_.map(_.through(decode).through(fs2.text.lines[F])))

  def value[F[_]](process: Process[F], decode: Pipe[F, Byte, String] = utf8.decode[F])
                 (using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, String]] =
    val iorT: IorT[F, Error, String] =
      for
        stream <- IorT(stdout[F](process))
        values <- stream.through(decode).compile.toList.asIT
      yield
        values.mkString.trim
    iorT.value
end process
