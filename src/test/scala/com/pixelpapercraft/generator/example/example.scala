package com.pixelpapercraft.generator
package example

import concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExportTopLevel

extension[A](a: A)
  def runIf[B >: A](cond: => Boolean)(f: A => B): B = if cond then f(a) else a

val background = Image(Base64Images.background)
val folds = Image(Base64Images.folds)

val showFolds = input.BooleanInput("Show Folds", true)
val skin = input.TextureInput("Skin", 64, 64, Seq(Texture()))

def setup(generator: Generator): Unit =
  generator.pages(0).draw(background, 0, 0)

def change(generator: Generator): Unit =
  skin.read().map { skinImage =>
    drawHead(generator, skinImage, 185, 117)
    if showFolds.read() then generator.pages(0).draw(folds, 0, 0)
  }

def drawHead(generator: Generator, skin: Texture, x: Int, y: Int) =
  def drawRect(src: (Int, Int, Int, Int), dst: (Int, Int, Int, Int), flip: Boolean) =
    generator.pages(0).draw(skin
      .crop(src._1, src._2, src._1 + src._3, src._2 + src._4)
      .scale(dst._3.toDouble / src._3, dst._4.toDouble / src._4)
      .runIf(flip)(img => img.flipVertical),
      src._1, src._2)

  // Head Base
  drawRect((0, 8, 8, 8), (x - 64, y + 0, 64, 64), false) // Right
  drawRect((8, 8, 8, 8), (x + 0, y + 0, 64, 64), false) // Face
  drawRect((16, 8, 8, 8), (x + 64, y + 0, 64, 64), false) // Left
  drawRect((24, 8, 8, 8), (x + 128, y + 0, 64, 64), false) // Back
  drawRect((8, 0, 8, 8), (x + 0, y - 64, 64, 64), false) // Top
  drawRect((16, 0, 8, 8), (x + 0, y + 64, 64, 64), true) // Bottom

  // Head Overlay
  drawRect((32, 8, 8, 8), (x - 64, y + 0, 64, 64), false) // Right
  drawRect((40, 8, 8, 8), (x + 0, y + 0, 64, 64), false) // Face
  drawRect((48, 8, 8, 8), (x + 64, y + 0, 64, 64), false) // Left
  drawRect((56, 8, 8, 8), (x + 128, y + 0, 64, 64), false) // Back
  drawRect((40, 0, 8, 8), (x + 0, y - 64, 64, 64), false) // Top
  drawRect((48, 0, 8, 8), (x + 0, y + 64, 64, 64), true) // Bottom

  generator

@JSExportTopLevel("exampleGenerator") val generator: Generator = Generator(
  id = "example",
  name = "Example Generator",
  thumbnail = (),
  video = (),
  instructions = "",
  pageAmount = 1,
  setup = setup,
  change = change,
  inputs = Seq(skin, showFolds)
)
