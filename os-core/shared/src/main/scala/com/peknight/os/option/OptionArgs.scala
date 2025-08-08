package com.peknight.os.option

import cats.syntax.applicative.*
import cats.syntax.functor.*
import cats.{Applicative, Functor}
import com.peknight.codec.Encoder

case class OptionArgs(flag: Boolean, args: Option[List[String]])
object OptionArgs:
  given optionArgsEncodeBoolean[F[_]: Applicative]: Encoder[F, OptionArgs, Boolean] =
    Encoder.map[F, OptionArgs, Boolean] {
      case true => OptionArgs(true, Some(List(String.valueOf(true))))
      case false => OptionArgs(false, Some(List(String.valueOf(false))))
    }

  given optionArgsEncodeUnit[F[_]: Applicative]: Encoder[F, OptionArgs, Unit] =
    Encoder.map[F, OptionArgs, Unit](_ => OptionArgs(true, Some(Nil)))

  given optionArgsEncodeA[F[_], A](using functor: Functor[F], encoder: Encoder[F, String, A]): Encoder[F, OptionArgs, A] =
    Encoder.instance[F, OptionArgs, A](a => encoder.encode(a).map(s => OptionArgs(true, Some(List(s)))))

  given optionArgsVectorEncodeA[F[_], A](using functor: Functor[F], encoder: Encoder[F, Vector[String], A])
  : Encoder[F, OptionArgs, A] =
    Encoder.instance[F, OptionArgs, A](a => encoder.encode(a).map(v => OptionArgs(true, Some(v.toList))))

  given optionArgsEncodeOptionA[F[_], A](using applicative: Applicative[F], encoder: Encoder[F, OptionArgs, A])
  : Encoder[F, OptionArgs, Option[A]] =
    Encoder.instance[F, OptionArgs, Option[A]] {
      case Some(a) => encoder.encode(a)
      case _ => OptionArgs(false, None).pure[F]
    }
end OptionArgs
