package com.peknight.os.option

import cats.syntax.eq.*
import spire.math.Interval

sealed trait OptionKey:
  def optionType: OptionType
  def key: String
  def argumentNumber: Interval[Int]
  def combinable: Boolean
end OptionKey
object OptionKey:
  case class LongOption(key: String, private val argNum: Interval[Int] = Interval.closed(0, 1)) extends OptionKey:
    def optionType: OptionType = OptionType.LongOption
    def argumentNumber: Interval[Int] = getArgumentNumber(argNum)
    def combinable: Boolean = false
  end LongOption
  case class ShortOption(private val ch: Char, private val argNum: Interval[Int] = Interval.closed(0, 1),
                         private val canCombine: Boolean = true) extends OptionKey:
    def optionType: OptionType = OptionType.ShortOption
    def key: String = s"$ch"
    def argumentNumber: Interval[Int] = getArgumentNumber(argNum)
    def combinable: Boolean = canCombine && argumentNumber === Interval.point(0)
  end ShortOption
  case class NonStandardOption(key: String, private val argNum: Interval[Int] = Interval.closed(0, 1)) extends OptionKey:
    def optionType: OptionType = OptionType.NonStandardOption
    def argumentNumber: Interval[Int] = getArgumentNumber(argNum)
    def combinable: Boolean = false
  end NonStandardOption
  case class BSDOption(key: String, private val argNum: Interval[Int] = Interval.closed(0, 1),
                       private val canCombine: Boolean = true) extends OptionKey:
    def optionType: OptionType = OptionType.BSDOption
    def argumentNumber: Interval[Int] = getArgumentNumber(argNum)
    def combinable: Boolean = canCombine && key.length == 1 && argumentNumber === Interval.point(0)
  end BSDOption

  private def getArgumentNumber(argNum: Interval[Int]): Interval[Int] =
    val interval = argNum & Interval.atOrAbove(0)
    if interval.isEmpty then Interval.point(0) else interval
end OptionKey
