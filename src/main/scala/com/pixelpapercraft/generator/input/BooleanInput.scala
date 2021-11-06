package com.pixelpapercraft.generator
package input

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("BooleanInput")
case class BooleanInput(label: String, default: Boolean)
  extends Input[Boolean](label):
  @JSExport
  override def read(): Boolean = default
