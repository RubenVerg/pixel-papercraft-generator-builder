package com.pixelpapercraft.generatorbuilder.builder.input

import com.pixelpapercraft.generatorbuilder.builder.{MutableItemBox, Texture}

import concurrent.ExecutionContext.Implicits.global
import com.pixelpapercraft.generatorbuilder.builder.render.RenderInputs

import scala.concurrent.Future
import scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}

/**
 * An input for a Minecraft texture
 *
 * @param label A textual description of this input
 * @param width Width of the default resolution
 * @param height Height of the default resolution
 * @param choices A set of suggested default choices
 */
@JSExportTopLevel("TextureInput")
case class TextureInput(label: String, width: Int, height: Int, choices: js.Array[Texture])
  extends Input[Future[Texture]](label):

  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create() =
    if (id().isEmpty)
      id() = Some(RenderInputs.createImage(label/*, choices*/))
    id().get

  override def read() =
    id().map(RenderInputs.getImage).getOrElse(Future.successful("data:image/png;base64,")).flatMap(Texture.load(_))

  @JSExport("read")
  def readJS() = js.Promise[Texture]{ (resolve, reject) =>
    read().andThen { result => result match {
      case Success(value) => resolve(value)
      case Failure(exception) => reject(exception)
    }}
  }

object TextureInput:
  def apply(label: String, width: Int, height: Int, choices: Seq[Texture]): TextureInput =
    TextureInput(label, width, height, js.Array(choices*))
