package com.peknight.os.option

trait OptionEncoderConfig:
  def transformOptionKey: String => OptionKey
end OptionEncoderConfig