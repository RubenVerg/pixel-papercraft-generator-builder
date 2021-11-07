package com.pixelpapercraft.generator
package render

import scalatags.JsDom.all.*

import scala.scalajs.js.{Date, undefined}
import org.scalajs.dom.{Blob, BlobPropertyBag, FileReader, HTMLInputElement, window}

import scala.concurrent.{Future, Promise}
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.scalajs.js.typedarray.Uint8Array
import scala.util.Random

def mkid(typ: String) = s"$typ-${Date.now()}-${Random.nextInt()}"

@JSExportTopLevel("__testRenderInputs")
@JSExportAll
object RenderInputs:
  def setupChangeReact(id: String)(f: Generator => Unit, generator: Generator) =
    window.document.querySelector(s"#$id").addEventListener("change", evt => f(generator))
  
  def createBoolean(lbl: String, default: Boolean) =
    val bid = mkid("boolean")
    window.document.body.appendChild(
      div(
        label(`for` := bid)(lbl),
        input(`type` := "checkbox", id := bid, if default then attr("checked") := "checked" else ())
      ).render
    )
    bid

  def getBoolean(id: String) =
    window.document.querySelector(s"#$id").asInstanceOf[HTMLInputElement].checked

  def createImage(lbl: String/*, choices: Seq[Image]*/) =
    val bid = mkid("image")
    window.document.body.appendChild(
      div(
        label(`for` := bid)(lbl),
        input(`type` := "file", id := bid)
      ).render
    )
    bid

  def getImage(id: String) =
    val input = window.document.querySelector(s"#$id").asInstanceOf[HTMLInputElement]
    if input.files.isEmpty then Future.successful("data:image/png;base64,")
    else
      val promise = Promise[String]()
      val file = input.files.item(0)
      val reader = FileReader()
      reader.addEventListener("loadend", event => promise.success(reader.result.asInstanceOf[String]))
      reader.readAsDataURL(file)
      promise.future

  def createButton(lbl: String, cb: () => Unit) =
    val bid = mkid("button")
    val btn = button(id := bid)(lbl).render
    btn.addEventListener("click", evt => cb())
    window.document.body.appendChild(div(btn).render)
    bid