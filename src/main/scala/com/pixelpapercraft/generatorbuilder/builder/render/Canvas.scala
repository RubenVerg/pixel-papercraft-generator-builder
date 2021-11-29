package com.pixelpapercraft.generatorbuilder.builder.render

import com.pixelpapercraft.generatorbuilder.builder.Image
import com.pixelpapercraft.generatorbuilder.builder.Image.ScaleAlgorithm
import org.scalajs.dom
import dom.{html, window}

import scala.concurrent.Promise

extension (el: dom.Element)
  def as[A <: dom.Element] = el.asInstanceOf[A]

extension (canv: html.Canvas)
  def ctx2d = canv.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

object Canvas:
  def makeCanvas(width: Double, height: Double) =
    val canv = window.document.createElement("canvas").as[html.Canvas]
    canv.width = width.toInt
    canv.height = height.toInt
    canv
  
  def crop(canvas: html.Canvas, start: (Int, Int), end: (Int, Int)) =
    val newCanvas = makeCanvas(end._1 - start._1, end._2 - start._2)
    newCanvas.ctx2d.drawImage(
      canvas,
      start._1, start._2, end._1 - start._1, end._2 - start._2,
      0, 0, end._1 - start._1, end._2 - start._2
    )
    newCanvas
    
  def scale(canvas: html.Canvas, dw: Int, dh: Int, algo: Image.ScaleAlgorithm) =
    def classic() =
      val newCanvas = makeCanvas(dw, dh)
      newCanvas.ctx2d.drawImage(canvas, 0, 0, dw, dh)
      newCanvas

    // Adapted from https://stackoverflow.com/a/7815428/11974245, this function is shared as CC BY-SA 3.0
    def nearestNeighbor() =
      val data = canvas.ctx2d.getImageData(0, 0, canvas.width, canvas.height)
      val newCanvas = makeCanvas(dw, dh)
      val ctx = newCanvas.ctx2d
      val factorX = dw / canvas.width.toDouble
      val factorY = dh / canvas.height.toDouble
      for 
        x <- 0 until canvas.width
        y <- 0 until canvas.height
        cellId = data.width * y + x
        r = data.data(cellId * 4 + 0)
        g = data.data(cellId * 4 + 1)
        b = data.data(cellId * 4 + 2)
        a = data.data(cellId * 4 + 3)
      do
        ctx.fillStyle = s"rgba($r, $g, $b, ${a / 255d})"
        ctx.fillRect(x * factorX, y * factorY, factorX, factorY)
      newCanvas
      
    algo match {
      case Image.ScaleAlgorithm.Classic => classic()
      case Image.ScaleAlgorithm.NearestNeighbor => nearestNeighbor()
    }
    
  def flipHorizontal(canvas: html.Canvas) =
    val newCanvas = makeCanvas(canvas.width, canvas.height)
    val ctx = newCanvas.ctx2d
    ctx.save()
    ctx.scale(-1, 1)
    ctx.drawImage(canvas, -canvas.width, 0)
    ctx.restore()
    newCanvas
    
  def flipVertical(canvas: html.Canvas) =
    val newCanvas = makeCanvas(canvas.width, canvas.height)
    val ctx = newCanvas.ctx2d
    ctx.save()
    ctx.scale(1, -1)
    ctx.drawImage(canvas, 0, -canvas.height)
    ctx.restore()
    newCanvas
    
  def blend(canvas: html.Canvas, color: (Int, Int, Int)) =
    val newCanvas = makeCanvas(canvas.width, canvas.height)
    val ctx = newCanvas.ctx2d
    val data = canvas.ctx2d.getImageData(0, 0, canvas.width, canvas.height)
    val newData = ctx.createImageData(data.width, data.height)
    for
      i <- 0 until data.data.length by 4
      r = (data.data(i) * color._1 / 255d).toInt
      g = (data.data(i + 1) * color._2 / 255d).toInt
      b = (data.data(i + 2) * color._3 / 255d).toInt
      a = data.data(i + 3)
    do
      newData.data(i) = r
      newData.data(i + 1) = g
      newData.data(i + 2) = b
      newData.data(i + 3) = a
    ctx.putImageData(newData, 0, 0)
    newCanvas

  def rotate(canvas: html.Canvas, point: (Int, Int), angle: Double) =
    val newCanvas = makeCanvas(math.max(canvas.width, canvas.height)/* * 2*/, math.max(canvas.width, canvas.height)/* * 2*/)
    val ctx = newCanvas.ctx2d
    ctx.save()
    ctx.translate(newCanvas.width / 2, newCanvas.height / 2)
    ctx.rotate(angle)
    ctx.drawImage(canvas, -point._1, -point._2)
    ctx.restore()
    newCanvas

  def drawImage(canvas: html.Canvas, image: Image, originX: Int, originY: Int) =
    canvas.ctx2d.drawImage(image.canvas, originX, originY)
