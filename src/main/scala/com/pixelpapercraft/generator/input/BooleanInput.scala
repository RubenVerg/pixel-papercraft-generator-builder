package com.pixelpapercraft.generator
package input

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("BooleanInput")
case class BooleanInput(label: String, default: Boolean)
  extends Input[Boolean](label):
  var id: Option[String] = None

  @JSExport
  override def create() =
    if (id.isEmpty)
      id = Some(RenderInputs.createBoolean(label, default))
    id.get

  @JSExport
  override def read(): Boolean = id.map(RenderInputs.getBoolean).getOrElse(default)
