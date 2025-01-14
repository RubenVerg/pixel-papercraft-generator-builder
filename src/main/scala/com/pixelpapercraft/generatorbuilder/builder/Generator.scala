package com.pixelpapercraft.generatorbuilder.builder

import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalajs.js
import js.JSConverters.*

/**
 * A generator
 * 
 * This is the class you want to export from your main files
 * @param id The generator's ID. Make this match with the export name in the `generators.js` file.
 * @param name The generator's readable name
 * @param thumbnail Currently unused, provide `()` or `undefined` or really any value you like
 * @param video Currently unused, provide `()` or `undefined` or really any value you like
 * @param instructions Instructions to be shown above the inputs
 * @param pageAmount How many pages this generator should have
 * @param setup A function to be called once to set up everything, and when an input changes to resetup everthing 
 * @param change A function to be called when an input changes, to draw stuff
 * @param inputs A list of [[com.pixelpapercraft.generatorbuilder.builder.input.Input]]s that the generator depends on
 */
@JSExportTopLevel("Generator")
case class Generator (
                      @JSExport id: String,
                      @JSExport name: String,
                      @JSExport thumbnail: Unit,
                      @JSExport video: Unit,
                      @JSExport instructions: String,
                      @JSExport pageAmount: Int,
                      // @JSExport images: Seq[Unit],
                      // @JSExport textures: Seq[Unit],
                      private val setup: js.Function1[Generator, Unit],
                      private val change: js.Function1[Generator, Unit],
                      @JSExport inputs: js.Array[input.Input[?]]
                    ):

  private[builder] var drawListener: (Page, Image, Int, Int) => Unit =
    (_: Page, _: Image, _: Int, _: Int) => println("if you see this, there's a bug somewhere")

  /**
   * The pages of this generator, access them to draw
   */
  @JSExport val pages: js.Array[Page] = js.Array(Seq.fill(pageAmount)(0).zipWithIndex.map((_, idx) => Page(this, idx))*)

  @JSExport def runSetup(generator: Generator) =
    for (input <- inputs) do
      RenderInputs.setupChangeReact(input.create())(onChange, generator)
    setup(generator)
  @JSExport def onChange(generator: Generator) = change(generator)

object Generator:
  def apply(id: String,
            name: String,
            thumbnail: Unit,
            video: Unit,
            instructions: String,
            pageAmount: Int,
            setup: Generator => Unit,
            change: Generator => Unit,
            inputs: Seq[input.Input[?]]): Generator = Generator(
    id = id,
    name = name,
    thumbnail = thumbnail,
    video = video,
    instructions = instructions,
    pageAmount = pageAmount,
    setup = setup,
    change = change,
    inputs = inputs.toJSArray
  )