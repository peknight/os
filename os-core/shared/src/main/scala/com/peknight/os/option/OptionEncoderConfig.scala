package com.peknight.os.option

trait OptionEncoderConfig:
  def transformMemberOptionKey: String => OptionKey
  def booleanAsFlag: Boolean
  def transformConstructorOption: String => Option[(OptionKey, List[String])]
end OptionEncoderConfig