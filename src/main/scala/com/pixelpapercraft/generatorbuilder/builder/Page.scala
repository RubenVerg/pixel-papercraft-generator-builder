package com.pixelpapercraft.generatorbuilder.builder

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * A page of the output, you don't really need to manually construct these
 */
@JSExportTopLevel("Page")
case class Page(generator: Generator, idx: Int):
  /**
   * Draws an image at the given coordinates
   * @param x x coordinate of where the image's top-left corner will end up
   * @param y y coordinate of where the image's top-left will end up
   */
  @JSExport
  def draw(image: Image, x: Int, y: Int) =
    generator.drawListener(this, image, x, y)
    
object Page:
  /**
   * A collection of page sizes
   */
  object Sizes:
    object A4:
      object px:
        val width = 595
        val height = 842

  /**
   * @see [[com.pixelpapercraft.generatorbuilder.builder.Page.Sizes]]
   */
  @JSExportTopLevel("PageSizes") val __js_PageSizes = new {
    @JSExport val A4 = new {
      @JSExport val px = new {
        @JSExport val width = Sizes.A4.px.width
        @JSExport val height = Sizes.A4.px.height
      }
    }
  }
