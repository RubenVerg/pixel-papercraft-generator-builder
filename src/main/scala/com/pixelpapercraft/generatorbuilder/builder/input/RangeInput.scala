package com.pixelpapercraft.generatorbuilder.builder
package input

import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * An input representing an imprecise number, as a range
 * @param label A textual description of the input
 * @param min Minimum value the range can take
 * @param max Maximum value the range can take
 * @param step How much to increase along the range
 */
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
