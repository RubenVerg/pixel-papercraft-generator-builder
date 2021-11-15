package com.pixelpapercraft.generator


import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

type Point = (Int, Int)

// Lazy images, they don't store the data, just an URL and a sequence of transformations
@JSExportTopLevel("Image")
case class Image(
                  @JSExport url: String,
                  @JSExport transformations: Seq[Image.Transformation] = Seq.empty[Image.Transformation]
                ):
  import Image.*, Transformation.*

  @JSExport
  def transform(transformation: Transformation) =
    this.copy(transformations = this.transformations :+ transformation)

  /**
   * Crop the image
   * @param startX x coordinate of the top-left point
   * @param startY y coordinate of the top-left point
   * @param endX x coordinate of the bottom-right point
   * @param endY y coordinate of the bottom-right point
   */
  @JSExport
  def crop(startX: Int, startY: Int, endX: Int, endY: Int) = transform(Crop((startX, startY), (endX, endY)))

  /**
   * Rotate the image around the (0, 0) origin point
   * @param angle Angle in degrees, counterclockwise, of rotation
   */
  @JSExport
  def rotate(angle: Double, origin: "top left" | "center") = transform(origin match {
    case "top left" => Rotate(angle, (0, 0))
    case "center" => RotateAroundCenter(angle)
  })

  /**
   * Rotate the image around the given rotational origin
   * @param angle Angle in degrees, counterclockwise, of rotation
   * @param originX x coordinate of the rotational origin
   * @param originY y coordinate of the rotational origin
   */
  @JSExport
  def rotate(angle: Double, originX: Int, originY: Int) = transform(Rotate(angle, (originX, originY)))

  /**
   * Scale the image by a factor of `factorX`*`factorY`
   */
  @JSExport
  def scale(factorX: Double, factorY: Double, algorithm: ScaleAlgorithm = ScaleAlgorithm.NearestNeighbor) =
    transform(Scale(factorX, factorY, algorithm))
    
  @JSExport
  def scaleTo(width: Int, height: Int, algorithm: ScaleAlgorithm = ScaleAlgorithm.NearestNeighbor) =
    transform(ScaleToSize(width, height, algorithm))

  /**
   * Flip the image on the x axis
   */
  @JSExport
  def flipVertical = transform(VerticalFlip)

  /**
   * Flip the image on the y axis
   */
  @JSExport
  def flipHorizontal = transform(HorizontalFlip)

  /**
   * Blends the image with a layer of the provided RGB color
   * @usecase This works the same way Minecraft colorizes grayscale textures, so getting colors from there should give you correct results
   */
  @JSExport
  def blend(r: Int, g: Int, b: Int) = transform(Blend(r, g, b))

object Image:
  // cannot be @JSExport'ed due to implementation details
  enum Transformation:
    /**
     * Move the current image origin to `p`
     * @note is this useless? can't really find any usecase, so no function on `Image`s
     */
    case Shift(p: Point)

    /**
     * Crop the image to the rectangular region delimited by `start` (top-left) and `end` (bottom-right)
     */
    case Crop(start: Point, end: Point)

    /**
     * Rotate the image `angle` degrees (full turn = 360) counterclockwise, using `origin` as the rotation origin
     */
    case Rotate(angle: Double, origin: Point)

    /**
     * Same as `Rotate`, except around the center
     */
    case RotateAroundCenter(angle: Double)

    /**
     * Scale the image with the `factor`s
     * @example Scale(0.5, 0.5) // scales the image to be half the size
     *          Scale(2, 3)   // scales the image to be twice the width and three times the height
     */
    case Scale(factorX: Double, factorY: Double, algorithm: ScaleAlgorithm)

    /**
     * Scale the image to be `width`*`height`
     */
    case ScaleToSize(width: Int, height: Int, algorithm: ScaleAlgorithm)

    /**
     * Flip the image vertically
     */
    case VerticalFlip

    /**
     * Flip the image horizontally
     */
    case HorizontalFlip

    /**
     * Multiplies the image with a layer of the required color
     */
    case Blend(r: Int, g: Int, b: Int)

  enum ScaleAlgorithm:
    case NearestNeighbor
    case Classic

  @JSExportTopLevel("ScaleAlgorithm") val __js_ScaleAlgorithm = new {
    @JSExport
    val NearestNeighbor = ScaleAlgorithm.NearestNeighbor
    @JSExport
    val Classic = ScaleAlgorithm.Classic
  }
