package com.peknight.os.group

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

sealed trait Group
object Group:
  case class GroupId(id: Int) extends Group:
    override def toString: String = id.toString
  end GroupId
  case class GroupName(name: String) extends Group:
    override def toString: String = name
  end GroupName

  def apply(value: String): Group = Try(GroupId(value.toInt)).getOrElse(GroupName(value))

  def groupId[F[_]](using MonadCancel[F, Throwable], Processes[F], Compiler[F, F]): F[GroupId] =
    ProcessBuilder("id", "-g").spawn[F].use(value(_)).map(_.toEither.flatMap(id => Try(GroupId(id.toInt)).toEither))
      .rethrow

  given stringCodecGroupId[F[_]: Applicative]: Codec[F, String, String, GroupId] =
    Codec.mapTry[F, String, String, GroupId](_.id.toString)(t => Try(GroupId(t.toInt)))

  given stringCodecGroupName[F[_]: Applicative]: Codec[F, String, String, GroupName] =
    Codec.map[F, String, String, GroupName](_.name)(GroupName.apply)

  given stringCodecGroup[F[_]: Applicative]: Codec[F, String, String, Group] =
    Codec.map[F, String, String, Group] {
      case GroupId(id) => id.toString
      case GroupName(name) => name
    }(Group.apply)

  given codecGroupId[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], GroupId] =
    Codec.codecS[F, S, GroupId]

  given codecGroupName[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], GroupName] =
    Codec.codecS[F, S, GroupName]

  given codecGroup[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], Group] =
    Codec.codecS[F, S, Group]
end Group
