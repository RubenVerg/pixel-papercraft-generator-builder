package com.pixelpapercraft.generator.input

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Text")
case class Text(text: String) extends Input[Nothing](text):
  @JSExport
  def read() = ???

  @JSExport
  def create() = RenderInputs.createText(text)
