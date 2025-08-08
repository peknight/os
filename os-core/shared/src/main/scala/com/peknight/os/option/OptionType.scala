package com.peknight.os.option

enum OptionType(val prefix: String):
  case LongOption extends OptionType("--")
  case ShortOption extends OptionType("-")
  case NonStandardOption extends OptionType("-")
  case BSDOption extends OptionType("")
end OptionType