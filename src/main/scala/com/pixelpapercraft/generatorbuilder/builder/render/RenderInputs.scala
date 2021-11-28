package com.pixelpapercraft.generatorbuilder.builder.render

import com.pixelpapercraft.generatorbuilder.builder.Generator
import scalatags.JsDom.all.*

import scala.scalajs.js.{Date, undefined}
import org.scalajs.dom.{Blob, BlobPropertyBag, FileReader, HTMLInputElement, HTMLSelectElement, window}

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

  def createSelect(lbl: String, opts: Seq[String]) =
    val bid = mkid("select")
    window.document.body.appendChild(div(
      select(id := bid)(opts.map { opt =>
        option(value := opt)(opt)
      })
    ).render)
    bid

  def getSelect(id: String) =
    window.document.querySelector(s"#$id").asInstanceOf[HTMLSelectElement].value

  def createText(text: String) =
    val bid = mkid("text")
    window.document.body.appendChild(div(
      p(id := bid)(text)
    ).render)
    bid

  def createRange(lbl: String, n: Double, x: Double, s: Double) =
    val bid = mkid("range")
    window.document.body.appendChild(div(
      label(`for` := bid)(lbl),
      input(`type` := "range", min := n, max := x, step := s, id := bid)
    ).render)
    bid

  def getRange(id: String) =
    window.document.querySelector(s"#$id").asInstanceOf[HTMLInputElement].value.toDouble

  def createNumber(lbl: String, n: Double, x: Double, s: Double) =
    val bid = mkid("number")
    window.document.body.appendChild(div(
      label(`for` := bid)(lbl),
      input(`type` := "number", min := n, max := x, step := s, id := bid)
    ).render)
    bid

  def getNumber(id: String) =
    window.document.querySelector(s"#$id").asInstanceOf[HTMLInputElement].value.toDouble

  def createInput(lbl: String, default: String) =
    val bid = mkid("input")
    window.document.body.appendChild(div(
      label(`for` := bid)(lbl),
      input(`type` := "text", value := default, id := bid)
    ).render)
    bid

  def getInput(id: String) =
    window.document.querySelector(s"#$id").asInstanceOf[HTMLInputElement].value

  def createColor(lbl: String, default: (Int, Int, Int)) =
    val bid = mkid("input")
    window.document.body.appendChild(div(
      label(`for` := bid)(lbl),
      input(`type` := "color", value := s"#${default._1.toHexString}${default._2.toHexString}${default._3.toHexString}", id := bid)
    ).render)
    bid

  def getColor(id: String) =
    val str = window.document.querySelector(s"#$id").asInstanceOf[HTMLInputElement].value.tail
    (Integer.parseInt(str.slice(0, 2), 16), Integer.parseInt(str.slice(2, 4), 16), Integer.parseInt(str.slice(4, 6), 16))