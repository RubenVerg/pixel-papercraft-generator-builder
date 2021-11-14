package com.pixelpapercraft.generator
package input

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalajs.js

@JSExportTopLevel("ButtonInput")
case class ButtonInput(label: String)(callback: js.Function0[Unit])
  extends Input[Unit](label):
  val id = MutableItemBox(Option.empty[String])
  
  @JSExport
  override def create(): String =
    if id().isEmpty then
      id() = Some(RenderInputs.createButton(label, callback))
    id().get
  
  @JSExport
  override def read(): Unit = ()
