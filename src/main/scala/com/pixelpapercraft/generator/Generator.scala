package com.pixelpapercraft.generator

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

case class Generator private[generator] (
                      @JSExport id: String,
                      @JSExport name: String,
                      @JSExport thumbnail: Unit,
                      @JSExport video: Unit,
                      @JSExport instructions: String,
                      @JSExport pageAmount: Int,
                      // @JSExport images: Seq[Unit],
                      // @JSExport textures: Seq[Unit],
                      private val setup: Generator => Unit,
                      private val change: Generator => Unit,
                      @JSExport inputs: Seq[input.Input[?]],
                      private[generator] drawListener: (Page, Image, Int, Int) => Unit
                    ):
  @JSExport val pages = Seq.fill(pageAmount)(0).zipWithIndex.map((_, idx) => Page(this, idx))

  @JSExport def runSetup(generator: Generator) =
    for (input <- inputs) do
      RenderInputs.setupChangeReact(input.create())(onChange, generator)
    setup(generator)
  @JSExport def onChange(generator: Generator) = change(generator)

object Generator:
  @JSExportTopLevel("Generator")
  def apply(
            id: String,
            name: String,
            thumbnail: Unit,
            video: Unit,
            instructions: String,
            pageAmount: Int,
            setup: Generator => Unit,
            change: Generator => Unit,
            inputs: Seq[input.Input[?]]
          ) = new Generator(
    id = id,
    name = name,
    thumbnail = thumbnail,
    video = video,
    instructions = instructions,
    pageAmount = pageAmount,
    setup = setup,
    change = change,
    inputs = inputs,
    drawListener = (_, _, _, _) => println("if you see this, there's a bug somewhere")
  )