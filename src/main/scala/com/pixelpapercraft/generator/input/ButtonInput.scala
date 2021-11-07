package com.pixelpapercraft.generator.input

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("ButtonInput")
case class ButtonInput(label: String)(callback: () => Unit)
  extends Input[Unit](label):
  var id: Option[String] = None
  
  @JSExport
  override def create(): String =
    if id.isEmpty then
      id = Some(RenderInputs.createButton(label, callback))
    id.get
  
  @JSExport
  override def read(): Unit = ()
