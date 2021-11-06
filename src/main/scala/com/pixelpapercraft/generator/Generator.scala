package com.pixelpapercraft.generator

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Generator")
case class Generator(
                      @JSExport id: String,
                      @JSExport name: String,
                      @JSExport thumbnail: Unit,
                      @JSExport video: Unit,
                      @JSExport instructions: String,
                      @JSExport pageAmount: Int,
                      // @JSExport images: Seq[Unit],
                      // @JSExport textures: Seq[Unit],
                      private val setup: Generator => Generator,
                      private val change: Generator => Generator,
                      @JSExport inputs: Seq[input.Input[?]]
                    ):
  @JSExport val pages = Seq.fill(pageAmount)(Page())
  @JSExport def runSetup(generator: Generator) = setup(generator)
  @JSExport def onChange(generator: Generator) = change(generator)