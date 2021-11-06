package com.pixelpapercraft.generator
package render

import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, window}

import scala.scalajs.js.annotation.JSExportTopLevel

object BarebonesRenderer:
  @JSExportTopLevel("BarebonesRenderer")
  def apply(generator: Generator) =
    window.document.body.innerHTML = ""
    window.document.body.appendChild(window.document.createElement("hr"))
    val canvases = generator.pages.map(page => window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement])
    canvases.foreach { _.width = 595 }
    canvases.foreach { _.height = 842 }
    val gen = generator.copy(drawListener = (page, image, x, y) => {
      Canvas.drawImage(canvases(page.idx), image, x, y)
    })
    gen.runSetup(gen)
    canvases.foreach { window.document.body.appendChild(_) }
