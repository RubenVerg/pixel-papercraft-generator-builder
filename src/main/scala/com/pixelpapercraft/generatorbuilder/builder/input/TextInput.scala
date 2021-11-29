package com.pixelpapercraft.generatorbuilder.builder
package input

import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * An input representing a free-form piece of text
 * @param label A textual description of the input
 * @param default The default text in the input
 */
@JSExportTopLevel("TextInput")
case class TextInput(label: String, default: String)
  extends Input[String](label):
  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create(): String =
    if id().isEmpty then
      id() = Some(RenderInputs.createInput(label, default))
    id().get

  @JSExport
  override def read() = id().map(RenderInputs.getInput(_)).get
