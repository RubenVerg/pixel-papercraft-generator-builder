lazy val root = project.in(file(".")).enablePlugins(ScalaJSPlugin)

name := "Pixel Papercraft Generator Builder"
normalizedName := "generator-builder"
organization := "com.pixelpapercraft"
version := "1.0.0-alpha.1"

scalaVersion := "3.1.0"
scalacOptions ++= "-feature -deprecation".split(" ").toSeq

libraryDependencies ++= Seq(

)

scalaJSUseMainModuleInitializer := false
scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }

Test / scalaJSUseMainModuleInitializer := false
Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
