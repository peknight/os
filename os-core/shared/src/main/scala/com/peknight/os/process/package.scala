package com.peknight.os

import cats.data.{Ior, IorT}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Monad, MonadError}
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.{asError, asIT}
import com.peknight.os.error.ProcessError
import fs2.io.process.Process
import fs2.text.utf8
import fs2.{Compiler, Pipe}

package object process:
  def processError[F[_]](process: Process[F])(using Monad[F], Compiler[F, F]): F[Error] =
    for
      messages <- process.stderr.through(utf8.decode[F]).through(fs2.text.lines[F]).compile.toList
      exitValue <- process.exitValue
    yield
      ProcessError.from(exitValue, messages)

  def isSuccess[F[_]](process: Process[F])(using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, Boolean]] =
    processError[F](process).asError.map(_.fold(Ior.left, e => Ior.both(e, e.success)))

  def value[F[_]](process: Process[F], decode: Pipe[F, Byte, String] = utf8.decode[F])
                 (using MonadError[F, Throwable], Compiler[F, F]): F[Ior[Error, String]] =
    val iorT: IorT[F, Error, String] =
      for
        _ <- IorT(processError[F](process).asError.map(_.fold(Ior.left, e =>
          if e.success then Ior.both(e, ()) else Ior.left(e)
        )))
        values <- process.stdout.through(decode).compile.toList.asIT
      yield
        values.mkString.trim
    iorT.value

end process
