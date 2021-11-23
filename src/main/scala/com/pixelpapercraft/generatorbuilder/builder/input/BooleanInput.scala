package com.pixelpapercraft.generatorbuilder.builder.input

import com.pixelpapercraft.generatorbuilder.builder.MutableItemBox
import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("BooleanInput")
case class BooleanInput(label: String, default: Boolean)
  extends Input[Boolean](label):
  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create() =
    if (id().isEmpty)
      id() = Some(RenderInputs.createBoolean(label, default))
    id().get

  @JSExport
  override def read(): Boolean = id().map(RenderInputs.getBoolean).getOrElse(default)