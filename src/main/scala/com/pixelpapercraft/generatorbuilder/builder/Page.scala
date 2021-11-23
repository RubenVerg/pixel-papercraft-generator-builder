package com.pixelpapercraft.generatorbuilder.builder

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Page")
case class Page(generator: Generator, idx: Int):
  /**
   * Draws an image at the given origin
   * @param x x coordinate of where the image's origin will end up
   * @param y y coordinate of where the image's origin will end up
   */
  @JSExport
  def draw(image: Image, x: Int, y: Int) =
    generator.drawListener(this, image, x, y)
    
object Page:
  object Sizes:
    object A4:
      object px:
        val width = 595
        val height = 842
        
  @JSExportTopLevel("PageSizes") val __js_PageSizes = new {
    @JSExport val A4 = new {
      @JSExport val px = new {
        @JSExport val width = Sizes.A4.px.width
        @JSExport val height = Sizes.A4.px.height
      }
    }
  }
