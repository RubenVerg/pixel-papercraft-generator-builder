package com.pixelpapercraft.generatorbuilder.builder
package input

import render.RenderInputs

import scala.scalajs.js
import js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("ColorInput")
case class ColorInput(label: String, defaultRed: Int, defaultGreen: Int, defaultBlue: Int)
  extends Input[(Int, Int, Int)](label):
  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create(): String =
    if id().isEmpty then
      id() = Some(RenderInputs.createColor(label, (defaultRed, defaultGreen, defaultBlue)))
    id().get

  override def read() = id().map(RenderInputs.getColor(_)).get

  @JSExport("read")
  def readJS() =
    val (r, g, b) = read()
    js.Array(r, g, b)

object ColorInput:
  def apply(label: String, default: (Int, Int, Int)): ColorInput = ColorInput(label, default._1, default._2, default._3)