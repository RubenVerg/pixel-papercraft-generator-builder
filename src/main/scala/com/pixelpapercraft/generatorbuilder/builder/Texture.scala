package com.pixelpapercraft.generatorbuilder.builder

import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.html

/**
 * Currently exactly the same as a [[com.pixelpapercraft.generatorbuilder.builder.Image]]
 */
@JSExportTopLevel("Texture")
class Texture(canv: html.Canvas) extends Image(canv)

object Texture:
  /**
   * @see [[com.pixelpapercraft.generatorbuilder.builder.Image.load]]
   */
  def load(url: String) = Image.load(url).map(img => Texture(img.canvas))

  /**
   * @see [[com.pixelpapercraft.generatorbuilder.builder.Image.loadJs]]
   */
  @JSExportTopLevel("loadTexture")
  def loadJs(url: String) = Image.loadJs(url).`then`(img => Texture(img.canvas))
