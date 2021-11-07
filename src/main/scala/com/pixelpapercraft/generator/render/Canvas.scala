package com.pixelpapercraft.generator
package render

import com.pixelpapercraft.generator.Image.{Transformation, ScaleAlgorithm}
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, HTMLImageElement, window}

import scala.concurrent.Promise

object Canvas:
  def drawImage(canvas: HTMLCanvasElement, image: Image, originX: Int, originY: Int) =
    val promise = Promise[Unit]()
    var tempCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
    tempCanvas.style.border = "2px dashed red"
    val tempImg = window.document.createElement("img").asInstanceOf[HTMLImageElement]
    tempImg.addEventListener("load", { evt =>
      tempCanvas.width = tempImg.width
      tempCanvas.height = tempImg.height
      var tempCtx = tempCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
      window.document.body.appendChild(tempCanvas)
      tempCtx.drawImage(tempImg, 0, 0)
      def willChangeCanvas(f: => HTMLCanvasElement) =
        val prevCanvas = tempCanvas
        val nowCanvas = f
        tempCanvas = nowCanvas
        tempCtx = tempCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        window.document.body.appendChild(nowCanvas)
        window.document.body.insertBefore(prevCanvas, nowCanvas)
        window.document.body.removeChild(prevCanvas)
      for (tr <- image.transformations) do
        tr match {
          case Transformation.Crop(start, end) => willChangeCanvas {
            val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
            transformCanvas.width = end._1 - start._1
            transformCanvas.height = end._2 - start._2
            transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].drawImage(
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
            val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
            transformCanvas.width = tempCanvas.width
            transformCanvas.height = tempCanvas.height
            transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].drawImage(
              tempCanvas, sx, sy
            )
            transformCanvas
          }
          case Transformation.Rotate(_, _) =>
            // TODO rotations, gotta calculate how much to have the canvas be long and stuff
            sys.process.stderr.println("ROTATION NOT SUPPORTED yet")
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
        }
      canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].drawImage(tempCanvas, originX, originY)
      // window.document.body.removeChild(tempCanvas)
    })
    tempImg.src = image.url
    promise.future

  def scaleClassic(canvas: HTMLCanvasElement, factorX: Double, factorY: Double) =
    val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
    transformCanvas.width = (canvas.width * factorX).toInt
    transformCanvas.height = (canvas.height * factorY).toInt
    transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
      .drawImage(canvas, 0, 0, canvas.width * factorX, canvas.height * factorY)
    transformCanvas

  // Adapted from https://stackoverflow.com/a/7815428/11974245, this function is shared as CC BY-SA 3.0
  def scaleNearestNeighbor(canvas: HTMLCanvasElement, factorX: Double, factorY: Double) =
    val data = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].getImageData(0, 0, canvas.width, canvas.height)
    val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
    transformCanvas.width = (canvas.width * factorX).toInt
    transformCanvas.height = (canvas.height * factorY).toInt
    val ctx = transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    for {
      x <- 0 until data.width
      y <- 0 until data.height
      cellIdx = (y * data.width + x * 4)
      red = data.data(cellIdx)
      green = data.data(cellIdx + 1)
      blue = data.data(cellIdx + 2)
      alpha = data.data(cellIdx + 3)
    } do
      ctx.fillStyle = s"rgba($red, $green, $blue, ${alpha / 255})"
      ctx.fillRect(x * factorX, y * factorY, factorX, factorY)
    transformCanvas