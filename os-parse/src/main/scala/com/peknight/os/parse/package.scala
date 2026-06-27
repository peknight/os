package com.peknight.os

import cats.data.NonEmptyList
import cats.parse.Parser

package object parse:
  private val doubleQuotedParser: Parser[String] =
    (Parser.string("""\"""").as("\"") | Parser.charWhere(_ != '"').string).rep
      .map(_.toList.mkString)
      .surroundedBy(Parser.char('"'))
  private val singleQuotedParser: Parser[String] =
    (Parser.string("""\'""").as("'") | Parser.charWhere(_ != '\'').string).rep
      .map(_.toList.mkString)
      .surroundedBy(Parser.char('\''))
  private val unquotedParser: Parser[String] =
    (Parser.string("""\ """).as(" ") | Parser.charWhere(!_.isWhitespace).string).rep.map(_.toList.mkString)
  private val wordParser: Parser[String] = doubleQuotedParser | singleQuotedParser | unquotedParser
  val commandParser: Parser[NonEmptyList[String]] = wordParser.repSep(Parser.charsWhile(_.isWhitespace).void)
end parse
