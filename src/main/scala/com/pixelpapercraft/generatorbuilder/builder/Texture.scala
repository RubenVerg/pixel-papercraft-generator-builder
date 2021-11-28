package com.pixelpapercraft.generatorbuilder.builder

import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.html

@JSExportTopLevel("Texture")
class Texture(canv: html.Canvas) extends Image(canv)

object Texture:
  def load(url: String) = Image.load(url).map(img => Texture(img.canvas))

  @JSExportTopLevel("loadTexture")
  def loadJs(url: String) = Image.loadJs(url).`then`(img => Texture(img.canvas))
