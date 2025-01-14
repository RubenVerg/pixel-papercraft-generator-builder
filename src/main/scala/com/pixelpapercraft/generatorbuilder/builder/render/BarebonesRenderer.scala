package com.pixelpapercraft.generatorbuilder.builder.render

import com.pixelpapercraft.generatorbuilder.builder.Generator
import com.pixelpapercraft.generatorbuilder.builder.Page.Sizes.A4
import com.pixelpapercraft.generatorbuilder.builder.Page.Sizes.A4.px
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, window}

import scala.scalajs.js.annotation.JSExportTopLevel

object BarebonesRenderer:
  @JSExportTopLevel("BarebonesRenderer")
  def apply(generator: Generator) =
    window.document.body.innerHTML = ""
    window.document.body.appendChild(window.document.createElement("hr"))
    val canvases = generator.pages.map(page => window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement])
    canvases.foreach { _.width = px.width }
    canvases.foreach { _.height = A4.px.height }
    val gen = generator.copy(change = g => {
      canvases.foreach { canv =>
        val ctx = canv.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        ctx.clearRect(0, 0, canv.width, canv.height)
      }
      generator.runSetup(g)
      try { generator.onChange(g) } catch {
        case ex: Throwable => sys.process.stderr.println(s"Exception thrown `onChange`! $ex")
      }
    })
    gen.drawListener = (page, image, x, y) => {
      Canvas.drawImage(canvases(page.idx), image, x, y)
    }
    gen.runSetup(gen)
    try { gen.onChange(gen) } catch {
      case _: Throwable => ()
    }
    canvases.foreach { window.document.body.appendChild(_) }
