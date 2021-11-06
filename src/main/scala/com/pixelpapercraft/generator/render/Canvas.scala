package com.pixelpapercraft.generator
package render

import com.pixelpapercraft.generator.Image.Transformation
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, HTMLImageElement, window}

import scala.concurrent.Promise

object Canvas:
  def drawImage(canvas: HTMLCanvasElement, image: Image, originX: Int, originY: Int) =
    val promise = Promise[Unit]()
    var tempCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
    val tempImg = window.document.createElement("img").asInstanceOf[HTMLImageElement]
    tempImg.addEventListener("load", { evt =>
      tempCanvas.width = tempImg.width
      tempCanvas.height = tempImg.height
      var tempCtx = tempCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
      tempCtx.drawImage(tempImg, 0, 0)
      for (tr <- image.transformations) do
        tr match {
          case Transformation.Crop(start, end) =>
            val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
            transformCanvas.width = end._1 - start._1
            transformCanvas.height = end._2 - start._2
            transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].drawImage(
              tempCanvas,
              start._1, start._2, end._1 - start._1, end._2 - start._2,
              0, 0, end._1 - start._1, end._2 - start._2
            )
            tempCanvas = transformCanvas
            tempCtx = tempCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
          case Transformation.Scale(fx, fy) =>
            val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
            transformCanvas.width = (tempCanvas.width * fx).toInt
            transformCanvas.height = (tempCanvas.height * fy).toInt
            transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
              .drawImage(tempCanvas, 0, 0, tempCanvas.width * fx, tempCanvas.height * fy)
            tempCanvas = transformCanvas
            tempCtx = tempCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
          case Transformation.Shift(sx, sy) =>
            val transformCanvas = window.document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
            transformCanvas.width = tempCanvas.width
            transformCanvas.height = tempCanvas.height
            transformCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].drawImage(
              tempCanvas, sx, sy
            )
            tempCanvas = transformCanvas
            tempCtx = tempCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
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
        }
      canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D].drawImage(tempCanvas, originX, originY)
    })
    tempImg.src = image.url
    promise.future