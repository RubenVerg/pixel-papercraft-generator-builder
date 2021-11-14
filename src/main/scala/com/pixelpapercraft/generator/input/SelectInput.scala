package com.pixelpapercraft.generator
package input

import com.pixelpapercraft.generator.render.RenderInputs

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalajs.js

@JSExportTopLevel("SelectInput")
case class SelectInput(label: String, options: js.Array[String])
  extends Input[String](label):
  def this(label: String, opts: Seq[String]) = this(label, js.Array(opts*))

  val id = MutableItemBox(Option.empty[String])

  @JSExport
  override def create(): String =
    if id().isEmpty then
      id() = Some(RenderInputs.createSelect(label, options.toSeq))
    id().get
  
  @JSExport
  override def read() = id().map(RenderInputs.getSelect(_)).get
