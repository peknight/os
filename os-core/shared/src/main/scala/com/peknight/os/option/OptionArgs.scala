package com.peknight.os.option

import cats.syntax.applicative.*
import cats.syntax.functor.*
import cats.{Applicative, Functor}
import com.peknight.codec.Encoder

case class OptionArgs(args: Option[List[String]])
object OptionArgs:
  def optionArgsEncodeBoolean[F[_]: Applicative]: Encoder[F, OptionArgs, Boolean] =
    Encoder.map[F, OptionArgs, Boolean] {
      case true => OptionArgs(Some(Nil))
      case false => OptionArgs(None)
    }

  given optionArgsEncodeA[F[_], A](using functor: Functor[F], encoder: Encoder[F, String, A]): Encoder[F, OptionArgs, A] =
    Encoder.instance[F, OptionArgs, A](a => encoder.encode(a).map(s => OptionArgs(Some(List(s)))))

  given optionArgsVectorEncodeA[F[_], A](using functor: Functor[F], encoder: Encoder[F, Vector[String], A])
  : Encoder[F, OptionArgs, A] =
    Encoder.instance[F, OptionArgs, A](a => encoder.encode(a).map(v => OptionArgs(Some(v.toList))))

  given optionArgsVectorEncodeOptionUnit[F[_]: Applicative]: Encoder[F, OptionArgs, Option[Unit]] =
    Encoder.map[F, OptionArgs, Option[Unit]] {
      case Some(_) => OptionArgs(Some(Nil))
      case _ => OptionArgs(None)
    }

  given optionArgsVectorEncodeOptionA[F[_], A](using applicative: Applicative[F], encoder: Encoder[F, OptionArgs, A])
  : Encoder[F, OptionArgs, Option[A]] =
    Encoder.instance[F, OptionArgs, Option[A]] {
      case Some(a) => encoder.encode(a)
      case _ => OptionArgs(None).pure[F]
    }
end OptionArgs
