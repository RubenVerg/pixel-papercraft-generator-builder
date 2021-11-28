package com.pixelpapercraft.generatorbuilder
package generators.example

import builder.*

import concurrent.ExecutionContext.Implicits.global
import scalajs.js
import js.annotation.JSExportTopLevel
import js.JSConverters.*

extension[A](a: A)
  def runIf[B >: A](cond: => Boolean)(f: A => B): B = if cond then f(a) else a

val background = Image.load(Base64Images.background)
val folds = Image.load(Base64Images.folds)

val showFolds = input.BooleanInput("Show Folds", true)
val skin = input.TextureInput("Skin", 64, 64, Seq())

def setup(generator: Generator): Unit =
  background.map { background =>
    generator.pages(0).draw(background, 0, 0)
  }

def change(generator: Generator): Unit =
  background.map { background => folds.map { folds =>
    skin.read().map { skinImage =>
      drawHead(generator, skinImage, 185, 117)
      if showFolds.read() then generator.pages(0).draw(folds, 0, 0)
    }
  }}

def drawHead(generator: Generator, skin: Texture, x: Int, y: Int) =
  def drawRect(src: (Int, Int, Int, Int), dst: (Int, Int, Int, Int), flip: Boolean) =
    println(
      s"""
         |Drawing section (${src._1}, ${src._2}) thru (${src._1 + src._3}, ${src._2 + src._4})
         |at (${dst._1}, ${dst._2}) thru (${dst._1 + dst._3}, ${dst._2 + dst._4})
         |scale: X*${dst._3.toDouble / src._3}, Y*${dst._4.toDouble / src._4}
         |${if flip then "" else "not "}flipped
         |""".stripMargin)
    generator.pages(0).draw(skin
      .crop(src._1, src._2, src._1 + src._3, src._2 + src._4)
      .scaleTo(dst._3, dst._4)
      .runIf(flip)(img => img.flipVertical),
      dst._1, dst._2)

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

@JSExportTopLevel("exampleGenerator") val generator: Generator = Generator(
  id = "exampleScala",
  name = "Example Generator (Scala)",
  thumbnail = (),
  video = (),
  instructions = "",
  pageAmount = 1,
  setup = setup,
  change = change,
  inputs = Seq(skin, showFolds)//.toJSArray
)
