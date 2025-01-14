package com.pixelpapercraft.generatorbuilder
package generators.testing

import builder.*
import Image.ScaleAlgorithm
import input.{ButtonInput, ColorInput}

import scalajs.js
import js.JSConverters.*
import js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global

val color = ColorInput("color test", (0x90, 0x81, 0x4d))

val grid = Image.load(Base64Images.grid)
val steve = Texture.load(Base64Images.steve)
val steve256 = Texture.load(Base64Images.steve256)
val grassSide = Texture.load(Base64Images.grassSide)
val grassTop = Texture.load(Base64Images.grassTop)

object Config:
  val colCount = 4
  val rowCount = 6
  val gridCellSize = 128
  val gridWidth = colCount * gridCellSize
  val gridHeight = rowCount * gridCellSize
  val offsetX = (Page.Sizes.A4.px.width - gridWidth) / 2
  val offsetY = (Page.Sizes.A4.px.height - gridHeight) / 2

def setup(generator: Generator) = ()

val button = ButtonInput("Example Button Input") { () =>
  println("Hello")
}

def change(generator: Generator, grid: Image, steve: Texture, steve256: Texture, grassSide: Texture, grassTop: Texture) =
  def drawGrid(generator: Generator, page: Int) =
    println(s"Drawing grid in page $page")
    for
      x <- 0 until Config.colCount
      y <- 0 until Config.colCount
      xPos = Config.offsetX + x * Config.gridCellSize
      yPos = Config.offsetY + y * Config.gridCellSize
    do
      generator.pages(page).draw(grid, xPos, yPos)

  def drawPage4(generator: Generator) =
    println("Drawing page 4")
    drawGrid(generator, 3)

    val page = generator.pages(3)

    val padding = 32
    val size = Config.gridCellSize - padding
    val indent = padding / 2

    val tints = Seq(
      "#90814D",
      "#BFB755",
      "#59C93C",
      "#64C73F",
      "#79C05A",
      "#88BB67",
      "#507A32",
      "#6A7039",
      "#4C763C",
      "#91BD59",
      "#8EB971",
      "#55C93F",
      "#8AB689",
      "#83B593",
      "#86B87F",
      "#86B783"
    ).map { hex =>
      val red = Integer.parseInt(hex.slice(1, 3), 16)
      val green = Integer.parseInt(hex.slice(3, 5), 16)
      val blue = Integer.parseInt(hex.slice(5, 7), 16)
      (red, green, blue)
    }.appended(color.read())

    for
      row <- 0 until Config.rowCount
      col <- 0 until Config.colCount
      x = Config.offsetX + Config.gridCellSize * col + indent
      y = Config.offsetY + Config.gridCellSize * row + indent
      index = col + row * Config.colCount
    do
      tints.lift(index) match {
        case None =>
          page.draw(steve.crop(8, 8, 16, 16).scaleTo(size, size), x, y)
          page.draw(grassSide.scaleTo(size, size).blend(89, 201, 60), x, y)
        case Some(tint) =>
          page.draw(grassTop.scaleTo(size, size).blend(tint._1, tint._2, tint._3), x, y)
      }

  def drawPage5(generator: Generator) =
    drawGrid(generator, 4)

    val page = generator.pages(4)

    val padding = 32
    val size = Config.gridCellSize - padding
    val indent = padding / 2

    val tests = Seq(
      (8, 8, 8, 8, size, size, false),
      (8, 8, 8, 8, size, size, true),
      (8, 8, 8, 8, size / 2, size, false),
      (8, 8, 8, 8, size / 2, size, true),
      (8, 8, 8, 8, size / 4, size, false),
      (8, 8, 8, 8, size / 4, size, true),
      (8, 8, 8, 8, size / 8, size, false),
      (8, 8, 8, 8, size / 8, size, true),
      (8, 8, 8, 8, size, size / 2, false),
      (8, 8, 8, 8, size, size / 2, true),
      (8, 8, 8, 8, size, size / 4, false),
      (8, 8, 8, 8, size, size / 4, true),
      (8, 8, 8, 8, size, size / 8, false),
      (8, 8, 8, 8, size, size / 8, true),
      (8, 8, 8, 8, size / 2, size / 2, false),
      (8, 8, 8, 8, size / 2, size / 2, true),
      (8, 8, 8, 8, size / 4, size / 4, false),
      (8, 8, 8, 8, size / 4, size / 4, true),
      (8, 8, 8, 8, size / 8, size / 8, false),
      (8, 8, 8, 8, size / 8, size / 8, true),
      (8, 8, 8, 8, 8, 8, true),
      (8, 8, 8, 8, 4, 4, true),
      (8, 8, 8, 8, 2, 2, true),
      (8, 8, 8, 8, 1, 1, true)
    )

    for
      row <- 0 until Config.rowCount
      col <- 0 until Config.colCount
      x = Config.offsetX + Config.gridCellSize * col + indent
      y = Config.offsetY + Config.gridCellSize * row + indent
      index = col + row * Config.colCount
      if index < tests.length
      (sx, sy, sw, sh, dw, dh, pixelate) = tests(index)
    // TODO pixelate!
    do
      page.draw(steve.crop(sx, sy, sx + sw, sy + sh).scaleTo(dw, dh), x, y)

  def drawPage(generator: Generator, pageIndex: Int, image: Image) =
    drawGrid(generator, pageIndex)

    val page = generator.pages(pageIndex)

    val things = Map[(Int, Int), (Double, 0 | 1 | -1)](
      (0, 0) -> (0, 0),
      (1, 0) -> (90, 0),
      (2, 0) -> (180, 0),
      (3, 0) -> (270, 0),
      (0, 1) -> (0, 1),
      (1, 1) -> (90, 1),
      (2, 1) -> (180, 1),
      (3, 1) -> (270, 1),
      (0, 2) -> (0, -1),
      (1, 2) -> (90, -1),
      (2, 2) -> (180, -1),
      (3, 2) -> (270, -1),
      (0, 3) -> (45, 0),
      (1, 3) -> (90, 0),
      (2, 3) -> (135, 0),
      (3, 3) -> (180, 0),
      (0, 4) -> (45, 1),
      (1, 4) -> (90, 1),
      (2, 4) -> (135, 1),
      (3, 4) -> (180, 1),
      (0, 4) -> (45, -1),
      (1, 4) -> (90, -1),
      (2, 4) -> (135, -1),
      (3, 4) -> (180, -1),
    )

    for
      ((x, y), (angle, flipMode)) <- things
    do
    page.draw(
      image.scaleTo(Config.gridCellSize / 2, Config.gridCellSize / 2),
      Config.offsetX + x * Config.gridCellSize + Config.gridCellSize / 4,
      Config.offsetY + y * Config.gridCellSize + Config.gridCellSize / 4
    )

  drawPage(generator, 0, steve.crop(8, 8, 16, 16))
  drawPage(generator, 1, steve256.crop(32, 32, 64, 64))

  drawGrid(generator, 2)
  val sw = 8
  val sh = 3
  val scale = 8
  val dw = sw * scale
  val dh = sh * scale
  val src = (8, 11, sw, sh)
  def dst(gridX: Int, gridY: Int) = (
    Config.offsetX + gridX * Config.gridCellSize + Config.gridCellSize / 2 - dw / 2,
    Config.offsetY + gridY * Config.gridCellSize + Config.gridCellSize / 2 - dh / 2,
    dw, dh
  )
  val rows = 6
  val cols = 4
  val deg = 360d / (rows * cols)

  for
    row <- 0 until rows
    col <- 0 until cols
    factor = row * cols + col
    rotate = deg * factor
    dest = dst(col, row)
  do
    generator.pages(2).draw(steve
      .crop(src._1, src._2, src._1 + src._3, src._2 + src._4)
      .scaleTo(dest._3, dest._4)
      .rotate(rotate),
      dest._1, dest._2
    )

  drawPage4(generator)
  drawPage5(generator)

@JSExportTopLevel("testingGenerator") val generator: Generator = Generator(
  id = "testing",
  name = "Testing",
  thumbnail = (),
  video = (),
  instructions = "",
  pageAmount = 5,
  setup = setup,
  change = (gen: Generator) => {
    grid.map { _grid =>
      steve.map { _steve =>
        steve256.map { _steve256 =>
          grassSide.map { _grassSide =>
            grassTop.map { _grassTop =>
              change(gen, _grid, _steve, _steve256, _grassSide, _grassTop)
            }
          }
        }
      }
    }
    ()
  },
  inputs = Seq(button, color)
)