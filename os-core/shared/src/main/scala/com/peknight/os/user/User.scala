package com.peknight.os.user

import cats.effect.MonadCancel
import cats.syntax.functor.*
import cats.syntax.monadError.*
import cats.{Applicative, Show}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.StringType
import com.peknight.os.process.value
import fs2.Compiler
import fs2.io.process.{ProcessBuilder, Processes}

import scala.util.Try

sealed trait User
object User:
  case class UserId(id: Int) extends User
  case class UserName(name: String) extends User

  def apply(value: String): User = Try(UserId(value.toInt)).getOrElse(UserName(value))

  def userId[F[_]](using MonadCancel[F, Throwable], Processes[F], Compiler[F, F]): F[UserId] =
    ProcessBuilder("id", "-u").spawn[F].use(value(_)).map(_.toEither.flatMap(id => Try(UserId(id.toInt)).toEither))
      .rethrow

  given stringCodecUserId[F[_]: Applicative]: Codec[F, String, String, UserId] =
    Codec.mapTry[F, String, String, UserId](_.id.toString)(t => Try(UserId(t.toInt)))

  given stringCodecUserName[F[_]: Applicative]: Codec[F, String, String, UserName] =
    Codec.map[F, String, String, UserName](_.name)(UserName.apply)

  given stringCodecUser[F[_]: Applicative]: Codec[F, String, String, User] =
    Codec.map[F, String, String, User] {
      case UserId(id) => id.toString
      case UserName(name) => name
    }(User.apply)

  given codecUserId[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], UserId] =
    Codec.codecS[F, S, UserId]

  given codecUserName[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], UserName] =
    Codec.codecS[F, S, UserName]

  given codecUser[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], User] =
    Codec.codecS[F, S, User]
end User
