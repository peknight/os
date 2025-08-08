package com.peknight.os.option

import cats.Applicative
import cats.syntax.applicative.*
import cats.syntax.apply.*
import cats.syntax.eq.*
import cats.syntax.functor.*
import com.peknight.codec.Encoder
import com.peknight.codec.obj.Object
import com.peknight.generic.Generic
import spire.math.Interval

case class Options(options: Object[OptionKey, List[String]]):
  def toList: List[String] =
    options.toList.groupBy((key, args) => if key.combinable && args.isEmpty then Some(key.optionType) else None)
      .map {
        case (Some(optionType), list) =>
          List(s"${optionType.prefix}${list.map((key, _) => key.key).mkString}")
        case (_, list) =>
          list.flatMap { (key, args) =>
            val k = s"${key.optionType.prefix}${key.key}"
            if args.length <= 0 then List(k)
            else if args.length == 1 || (key.argumentNumber & Interval.above(1)).nonEmpty then k :: args
            else args.flatMap(arg => List(k, arg))
          }
      }
      .toList
      .flatten
end Options
object Options:
  def derivedEncoderProduct[F[_], A](using config: OptionEncoderConfig)
                                    (using applicative: Applicative[F],
                                     instances: => Generic.Product.Instances[[X] =>> Encoder[F, OptionArgs, X], A])
  : Encoder[F, Options, A] =
    (a: A) => instances.foldRightWithLabel(a)(List.empty[(OptionKey, List[String])].pure[F]) {
      [X] => (encoder: Encoder[F, OptionArgs, X], x: X, label: String, acc: F[List[(OptionKey, List[String])]]) =>
        (encoder.encode(x), acc).mapN { (args, acc) =>
          val key = config.transformMemberOptionKey(label)
          if key.argumentNumber === Interval.point(0) || (x.isInstanceOf[Boolean] && config.booleanAsFlag && key.argumentNumber.contains(0)) then
            if args.flag then (key, Nil) :: acc
            else acc
          else
            args.args match
              case Some(args) => (key, args) :: acc
              case _ => acc
        }
    }.map(f => Options(Object.fromIterable(f)))

end Options