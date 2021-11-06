package com.pixelpapercraft.generator
package input

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * An input for a Minecraft texture
 *
 * @param label This texture's name
 * @param width Width of the default resolution
 * @param height Height of the default resolution
 * @param choices A set of suggested default choices
 */

@JSExportTopLevel("TextureInput")
case class TextureInput(label: String, width: Int, height: Int, choices: Seq[Texture])
  extends Input[Texture](label):
  @JSExport
  override def read() = if choices.isEmpty then ??? else choices.head
