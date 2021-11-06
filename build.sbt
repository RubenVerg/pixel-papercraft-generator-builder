lazy val root = project.in(file(".")).enablePlugins(ScalaJSPlugin)

name := "Pixel Papercraft Generator Builder"
normalizedName := "generator-builder"
organization := "com.pixelpapercraft"
version := "1.0.0-alpha.1"

scalaVersion := "3.1.0"
scalacOptions ++= "-feature -deprecation".split(" ").toSeq

libraryDependencies ++= Seq(
  // "org.scala-js" %%% "scalajs-dom" % "2.0.0",
  "com.lihaoyi" %%% "scalatags" % "0.10.0" cross CrossVersion.for3Use2_13
)

scalaJSUseMainModuleInitializer := false
scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }

Test / scalaJSUseTestModuleInitializer := false
Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }
