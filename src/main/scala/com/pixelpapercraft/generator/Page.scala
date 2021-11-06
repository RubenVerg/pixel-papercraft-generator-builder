package com.pixelpapercraft.generator

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Page")
case class Page():
  /**
   * Draws an image at the given origin
   * @param x x coordinate of where the image's origin will end up
   * @param y y coordinate of where the image's origin will end up
   */
  @JSExport
  def draw(image: Image, x: Int, y: Int) =
    sys.process.stderr.println(s"Draw $image at ($x, $y)")
