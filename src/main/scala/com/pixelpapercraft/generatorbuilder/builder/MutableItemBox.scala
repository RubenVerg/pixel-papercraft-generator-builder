package com.pixelpapercraft.generatorbuilder.builder

case class MutableItemBox[A](private var contained: A):
  def apply() = contained
  def update(a: A) =
    contained = a