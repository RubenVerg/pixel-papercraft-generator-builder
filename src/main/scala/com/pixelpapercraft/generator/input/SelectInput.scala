package com.pixelpapercraft.generator.input

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("SelectInput")
case class SelectInput[A <: String](label: String, options: Seq[A])
  extends Input[A](label):
  var id: Option[String] = None

  @JSExport
  override def create(): String =
    if id.isEmpty then
      id = Some(RenderInputs.createSelect(label, options))
    id.get
  
  @JSExport
  override def read(): A = id.map(RenderInputs.getSelect(_).asInstanceOf[A]).get
