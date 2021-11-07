lazy val root = project.in(file(".")).enablePlugins(ScalaJSPlugin)

name := "Pixel Papercraft Generator Builder"
normalizedName := "generator-builder"
organization := "com.pixelpapercraft"
version := "1.0.0-alpha.1"

scalaVersion := "3.1.0"
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Werror"
)

libraryDependencies ++= Seq(
  // "org.scala-js" %%% "scalajs-dom" % "2.0.0",
  "com.lihaoyi" %%% "scalatags" % "0.10.0" cross CrossVersion.for3Use2_13
)

scalaJSUseMainModuleInitializer := false
scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }

Test / scalaJSUseTestModuleInitializer := false
Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }

// https://alexn.org/blog/2020/05/26/scala-fatal-warnings.html

val filterConsoleScalacOptions = { options: Seq[String] =>
  options.filterNot(Set(
    "-Xfatal-warnings",
    "-Werror",
    "-Wdead-code",
    "-Wunused:imports",
    "-Ywarn-unused:imports",
    "-Ywarn-unused-import",
    "-Ywarn-dead-code",
  ))
}

console / scalacOptions ~= filterConsoleScalacOptions
Test / console / scalacOptions ~= filterConsoleScalacOptions