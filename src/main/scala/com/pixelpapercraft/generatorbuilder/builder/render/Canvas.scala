package com.pixelpapercraft.generatorbuilder.builder.render

import com.pixelpapercraft.generatorbuilder.builder.Image
import com.pixelpapercraft.generatorbuilder.builder.Image.{ScaleAlgorithm, Transformation}
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

  def drawImage(canvas: html.Canvas, image: Image, originX: Int, originY: Int) =
    val promise = Promise[Unit]()
    var tempCanvas = makeCanvas(0, 0)
    tempCanvas.style.border = "2px dashed red"
    val tempImg = window.document.createElement("img").as[html.Image]
    tempImg.addEventListener("load", { evt =>
      tempCanvas.width = tempImg.width
      tempCanvas.height = tempImg.height
      var tempCtx = tempCanvas.ctx2d
      window.document.body.appendChild(tempCanvas)
      tempCtx.drawImage(tempImg, 0, 0)
      def willChangeCanvas(f: => html.Canvas) =
        val prevCanvas = tempCanvas
        val nowCanvas = f
        tempCanvas = nowCanvas
        tempCtx = tempCanvas.ctx2d
        window.document.body.appendChild(nowCanvas)
        window.document.body.insertBefore(prevCanvas, nowCanvas)
        window.document.body.removeChild(prevCanvas)
      for (tr <- image.transformations) do
        tr match {
          case Transformation.Crop(start, end) => willChangeCanvas {
            val transformCanvas = makeCanvas(end._1 - start._1, end._2 - start._2)
            transformCanvas.ctx2d.drawImage(
              tempCanvas,
              start._1, start._2, end._1 - start._1, end._2 - start._2,
              0, 0, end._1 - start._1, end._2 - start._2
            )
            transformCanvas
          }
          case Transformation.Scale(fx, fy, alg) => willChangeCanvas {
            alg match {
              case ScaleAlgorithm.NearestNeighbor => scaleNearestNeighbor(tempCanvas, fx, fy)
              case ScaleAlgorithm.Classic => scaleClassic(tempCanvas, fx, fy)
            }
          }
          case Transformation.Shift(sx, sy) => willChangeCanvas {
            val transformCanvas = makeCanvas(tempCanvas.width, tempCanvas.height)
            transformCanvas.ctx2d.drawImage(tempCanvas, sx, sy)
            transformCanvas
          }
          case Transformation.RotateAroundCenter(degrees) =>
            willChangeCanvas(rotate(tempCanvas, (tempCanvas.width / 2, tempCanvas.height / 2), math.toRadians(degrees)))
          case Transformation.Rotate(degrees, p) =>
            willChangeCanvas(rotate(tempCanvas, p, math.toRadians(degrees)))
          case Transformation.HorizontalFlip =>
            tempCtx.save()
            tempCtx.scale(-1, 1)
            tempCtx.drawImage(tempCanvas, -tempCanvas.width, 0)
            tempCtx.restore()
          case Transformation.VerticalFlip =>
            tempCtx.save()
            tempCtx.scale(1, -1)
            tempCtx.drawImage(tempCanvas, 0, -tempCanvas.height)
            tempCtx.restore()
          case Transformation.ScaleToSize(w, h, alg) => willChangeCanvas {
            alg match {
              case ScaleAlgorithm.NearestNeighbor => scaleNearestNeighbor(tempCanvas, w.toDouble / tempCanvas.width, h.toDouble / tempCanvas.height)
              case ScaleAlgorithm.Classic => scaleClassic(tempCanvas, w.toDouble / tempCanvas.width, h.toDouble / tempCanvas.height)
            }
          }
          case Transformation.Blend(r, g, b) => willChangeCanvas {
            val transformationCanvas = makeCanvas(tempCanvas.width, tempCanvas.height)
            val transformationCtx = transformationCanvas.ctx2d
            val imageData = tempCtx.getImageData(0, 0, tempCanvas.width, tempCanvas.height)
            val newImageData = transformationCtx.createImageData(imageData.width, imageData.height)
            for
              i <- 0 until imageData.data.length by 4
              red = (imageData.data(i) * r / 255d).toInt
              green = (imageData.data(i + 1) * g / 255d).toInt
              blue = (imageData.data(i + 2) * b / 255d).toInt
              alpha = imageData.data(i + 3)
            do
              newImageData.data(i) = red
              newImageData.data(i + 1) = green
              newImageData.data(i + 2) = blue
              newImageData.data(i + 3) = alpha
            transformationCtx.putImageData(newImageData, 0, 0)
            transformationCanvas
          }
        }
      canvas.ctx2d.drawImage(tempCanvas, originX, originY)
      // window.document.body.removeChild(tempCanvas)
    })
    tempImg.src = image.url
    promise.future

  def scaleClassic(canvas: html.Canvas, factorX: Double, factorY: Double) =
    val transformCanvas = makeCanvas(canvas.width * factorX, canvas.height * factorY)
    transformCanvas.ctx2d.drawImage(canvas, 0, 0, canvas.width * factorX, canvas.height * factorY)
    transformCanvas

  // Adapted from https://stackoverflow.com/a/7815428/11974245, this function is shared as CC BY-SA 3.0
  def scaleNearestNeighbor(canvas: html.Canvas, factorX: Double, factorY: Double) =
    val data = canvas.ctx2d.getImageData(0, 0, canvas.width, canvas.height)
    val transformCanvas = makeCanvas(canvas.width * factorX, canvas.height * factorY)
    val ctx = transformCanvas.ctx2d
    for
      x <- 0 until canvas.width
      y <- 0 until canvas.height
      cellId = data.width * y + x
      red = data.data(cellId * 4 + 0)
      green = data.data(cellId * 4 + 1)
      blue = data.data(cellId * 4 + 2)
      alpha = data.data(cellId * 4 + 3)
    do
      // println(s"Drawing $factorX * $factorY rect at $x, $y")
      ctx.fillStyle = s"rgba($red, $green, $blue, ${alpha / 255})"
      ctx.fillRect(x * factorX, y * factorY, factorX, factorY)
    transformCanvas

  def rotate(canvas: html.Canvas, point: (Int, Int), angle: Double) =
    val transformationCanvas = makeCanvas(math.max(canvas.width, canvas.height)/* * 2*/, math.max(canvas.width, canvas.height)/* * 2*/)
    val transformationCtx = transformationCanvas.ctx2d
    transformationCtx.save()
    transformationCtx.translate(transformationCanvas.width / 2, transformationCanvas.height / 2)
    transformationCtx.rotate(angle)
    transformationCtx.drawImage(canvas, -point._1, -point._2)
    transformationCtx.restore()
    transformationCanvas