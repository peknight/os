package com.peknight.os.env

import cats.syntax.traverse.*
import cats.{Applicative, Show}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.error.DecodingFailure
import com.peknight.codec.sum.StringType

import scala.util.Try

case class Path(path: List[fs2.io.file.Path]):
  override def toString: String = path.map(_.toString).mkString(":")
end Path
object Path:
  given stringCodecPath[F[_]: Applicative]: Codec[F, String, String, Path] =
    Codec.applicative[F, String, String, Path](_.toString)(_.split(":").toList
      .traverse(path => Try(fs2.io.file.Path(path)).toEither.left.map(DecodingFailure.apply))
      .map(Path.apply)
    )
  given codecPath[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], Path] = Codec.codecS[F, S, Path]
end Path
