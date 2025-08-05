package com.peknight.os.option

import com.peknight.codec.Encoder
import com.peknight.generic.Generic

case class Options(options: List[String])
object Options:
  given encodeOptions[F[_], A](using instances: => Generic.Product.Instances[[X] =>> Encoder[F, OptionArgs, X], A])
  : Encoder[F, Options, A] with
    def encode(a: A): F[Options] =
      ???
  end encodeOptions
end Options
