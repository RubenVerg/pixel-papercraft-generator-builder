package com.pixelpapercraft.generator

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

type Point = (Int, Int)

// Lazy images, they don't store the data, just an URL and a sequence of transformations
@JSExportTopLevel("Image")
case class Image(
                  @JSExport url: String,
                  @JSExport transformations: Seq[Image.Transformation] = Seq.empty[Image.Transformation]
                ):
  import Image.Transformation, Transformation.*

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
  def rotate(angle: Double) = transform(Rotate(angle, (0, 0)))

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
  def scale(factorX: Double, factorY: Double) = transform(Scale(factorX, factorY))

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
     * Scale the image with the `factor`s
     * @example Scale(0.5, 0.5) // scales the image to be half the size
     *          Scale(2, 3)   // scales the image to be twice the width and three times the height
     */
    case Scale(factorX: Double, factorY: Double)

    /**
     * Flip the image vertically
     */
    case VerticalFlip

    /**
     * Flip the image horizontally
     */
    case HorizontalFlip
