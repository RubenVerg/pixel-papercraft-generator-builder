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
    canvases.foreach { _.width = Page.Sizes.A4.px.width }
    canvases.foreach { _.height = Page.Sizes.A4.px.height }
    val gen = generator.copy(drawListener = (page, image, x, y) => {
      Canvas.drawImage(canvases(page.idx), image, x, y)
    }, change = g => {
      canvases.foreach { canv =>
        val ctx = canv.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        ctx.clearRect(0, 0, canv.width, canv.height)
      }
      generator.runSetup(g)
      try { generator.onChange(g) } catch {
        case ex: Throwable => sys.process.stderr.println(s"Exception thrown `onChange`! $ex")
      }
    })
    gen.runSetup(gen)
    try { gen.onChange(gen) } catch {
      case _: Throwable => ()
    }
    canvases.foreach { window.document.body.appendChild(_) }
