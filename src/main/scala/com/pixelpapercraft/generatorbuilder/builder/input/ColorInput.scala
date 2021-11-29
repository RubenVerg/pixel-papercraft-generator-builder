package com.pixelpapercraft.generatorbuilder.builder
package input

import render.RenderInputs

import scala.scalajs.js
import js.annotation.{JSExport, JSExportTopLevel}

/**
 * An input representing a color swatch or wheel
 * @param label A textual description of the input
 * @param defaultRed The red component of the default color [0, 255]
 * @param defaultGreen The green component of the default color [0, 255]
 * @param defaultBlue The blue component of the default color [0, 255]
 */
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