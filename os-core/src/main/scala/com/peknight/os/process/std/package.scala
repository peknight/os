package com.peknight.os.process

import cats.data.Ior
import cats.effect.Async
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asError
import com.peknight.os.error.ProcessError
import fs2.io.process.Process
import fs2.io.{stderr, stdout}
import fs2.text.utf8
import fs2.{Compiler, Stream}

package object std:
  def isSuccess[F[_]: Async](process: Process[F]): F[Ior[Error, Boolean]] =
    val f: F[Error] =
      for
        messages <- process.stderr.observe(stderr[F]).through(utf8.decode[F]).through(fs2.text.lines[F])
          .concurrently(process.stdout.through(stdout[F]))
          .compile.toList
        exitValue <- process.exitValue
      yield
        ProcessError.from(exitValue, messages)
    f.asError.map(_.fold(error => Ior.left(error), error => Ior.both(error, error.success)))
end std
