package com.pixelpapercraft.generatorbuilder.builder

import render.Canvas
import org.scalajs.dom
import dom.html

import scala.scalajs.js
import js.annotation.{JSExport, JSExportTopLevel}
import scala.concurrent.Promise
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

type Point = (Int, Int)

/**
 * An `Image` represents the core class for any image in your code, that can be transformed
 * @param canvas The underlying canvas containing this image's data
 */
@JSExportTopLevel("Image")
case class Image(canvas: html.Canvas):
  import Image.*

  def width = canvas.width
  def height = canvas.height

  /**
   * Crop the image
   * @param startX x coordinate of the top-left point
   * @param startY y coordinate of the top-left point
   * @param endX x coordinate of the bottom-right point
   * @param endY y coordinate of the bottom-right point
   */
  @JSExport
  def crop(startX: Int, startY: Int, endX: Int, endY: Int) =
    Image(Canvas.crop(canvas, (startX, startY), (endX, endY)))

  /**
   * Rotate the image around the given rotational origin
   * @param angle Angle in degrees, counterclockwise, of rotation
   * @param originX x coordinate of the rotational origin
   * @param originY y coordinate of the rotational origin
   */
  @JSExport
  def rotate(angle: Double, originX: Int = width / 2, originY: Int = height / 2) =
    Image(Canvas.rotate(canvas, (originX, originY), math.toRadians(angle)))

  /**
   * Scale the image by a factor of `factorX`*`factorY`
   */
  @JSExport
  def scaleBy(factorX: Double, factorY: Double, algorithm: ScaleAlgorithm = ScaleAlgorithm.NearestNeighbor) =
    Image(Canvas.scale(canvas, (width * factorX).toInt, (height * factorY).toInt, algorithm))

  /**
   * Scale the image to be `width` * `height`
   */
  @JSExport
  def scaleTo(width: Int, height: Int, algorithm: ScaleAlgorithm = ScaleAlgorithm.NearestNeighbor) =
    Image(Canvas.scale(canvas, width, height, algorithm))

  /**
   * Flip the image on the x axis
   */
  @JSExport
  def flipVertical = Image(Canvas.flipVertical(canvas))

  /**
   * Flip the image on the y axis
   */
  @JSExport
  def flipHorizontal = Image(Canvas.flipHorizontal(canvas))

  /**
   * Blends the image with a layer of the provided RGB color
   * @usecase This works the same way Minecraft colorizes grayscale textures, so getting colors from there should give you correct results
   */
  @JSExport
  def blend(r: Int, g: Int, b: Int) = Image(Canvas.blend(canvas, (r, g, b)))

object Image:
  enum ScaleAlgorithm:
    case NearestNeighbor
    case Classic

  @JSExportTopLevel("ScaleAlgorithm") val __js_ScaleAlgorithm = new {
    @JSExport
    val NearestNeighbor = ScaleAlgorithm.NearestNeighbor
    @JSExport
    val Classic = ScaleAlgorithm.Classic
  }

  /**
   * Asyncronously loads an image from an URL and then pastes it onto a canvas and gives that to an `Image`
   * @param url URL to fetch the image from. CORS and friends apply.
   * @return A [[scala.concurrent.Future]] containing the image. The exported JavaScript function `loadImage` returns a [[scala.scalajs.js.Promise]] instead.
   */
  def load(url: String) =
    val promise = Promise[Image]()
    val canvas = Canvas.makeCanvas(0, 0)
    val img = dom.window.document.createElement("img").asInstanceOf[html.Image]
    img.addEventListener("load", { evt =>
      canvas.width = img.width
      canvas.height = img.height
      canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D].drawImage(img, 0, 0)
      promise.success(Image(canvas))
    })
    img.src = url
    promise.future

  /**
   * @see [[com.pixelpapercraft.generatorbuilder.builder.Image.load()]]
   */
  @JSExportTopLevel("loadImage")
  def loadJs(url: String) = js.Promise[Image]{ (resolve, reject) =>
    load(url).andThen {
      case Success(img) => resolve(img)
      case Failure(ex) => reject(ex)
    }
  }

