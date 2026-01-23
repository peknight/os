package com.peknight.os

sealed trait OS derives CanEqual
object OS:
  sealed trait Apple extends OS
  sealed trait Desktop extends OS
  sealed trait Mobile extends OS
  case object MacOS extends Desktop with Apple
  case object iOS extends Mobile with Apple
end OS
