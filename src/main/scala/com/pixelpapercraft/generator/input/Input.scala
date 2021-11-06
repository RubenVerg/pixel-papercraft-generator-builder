package com.pixelpapercraft.generator.input

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

abstract class Input[A](label: String):
  def read(): A// = ???
