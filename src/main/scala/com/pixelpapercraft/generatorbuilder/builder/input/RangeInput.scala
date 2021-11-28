package com.pixelpapercraft.generatorbuilder.builder
package input

import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("RangeInput")
case class RangeInput(label: String, min: Double = 0, max: Double = 10, step: Double = 0.5)
  extends Input[Double](label):
  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create(): String =
    if id().isEmpty then
      id() = Some(RenderInputs.createRange(label, min, max, step))
    id().get

  @JSExport
  override def read() = id().map(RenderInputs.getRange(_)).get
