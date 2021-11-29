package com.pixelpapercraft.generatorbuilder.builder.input

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * An abstract class representing an input on the page
 * @param label A textual description of the input
 * @tparam A The type that this input contains (subtype of [[scala.scalajs.js.Promise]] if the input reading must be asynchronous due to DOM API limitations)
 */
abstract class Input[A](label: String):
  /**
   * Create an instance of this input
   * @return The ID of the input assigned by the renderer
   */
  def create(): String

  /**
   * Read the input type
   */
  def read(): A
