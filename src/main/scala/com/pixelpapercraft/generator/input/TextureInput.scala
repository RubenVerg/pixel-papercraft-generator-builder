package com.pixelpapercraft.generator
package input

import concurrent.ExecutionContext.Implicits.global

import com.pixelpapercraft.generator.render.RenderInputs

import scala.concurrent.Future
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * An input for a Minecraft texture
 *
 * @param label This texture's name
 * @param width Width of the default resolution
 * @param height Height of the default resolution
 * @param choices A set of suggested default choices
 */

@JSExportTopLevel("TextureInput")
case class TextureInput(label: String, width: Int, height: Int, choices: Seq[Texture])
  extends Input[Future[Texture]](label):
  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create() =
    if (id().isEmpty)
      id() = Some(RenderInputs.createImage(label/*, choices*/))
    id().get

  @JSExport
  override def read() =
    id().map(RenderInputs.getImage).getOrElse(Future.successful("data:image/png;base64,")).map(Texture(_))
