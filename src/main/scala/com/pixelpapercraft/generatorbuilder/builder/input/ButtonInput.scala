package com.pixelpapercraft.generatorbuilder.builder.input

import com.pixelpapercraft.generatorbuilder.builder.MutableItemBox
import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalajs.js

@JSExportTopLevel("ButtonInput")
case class ButtonInput(label: String, callback: js.Function0[Unit])
  extends Input[Unit](label):
  val id = MutableItemBox(Option.empty[String])
  
  @JSExport
  override def create(): String =
    if id().isEmpty then
      id() = Some(RenderInputs.createButton(label, callback))
    id().get
  
  @JSExport
  override def read(): Unit = ()

object ButtonInput:
  def apply(label: String)(callback: () => Unit): ButtonInput = ButtonInput(label, callback)
