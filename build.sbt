lazy val builder = project.in(file(".")).enablePlugins(ScalaJSPlugin)
lazy val generators = project.in(file("generators")).enablePlugins(ScalaJSPlugin).dependsOn(builder)

builder / name := "Pixel Papercraft Generator Builder"
builder / normalizedName := "builder"
generators / name := "Pixel Papercraft Generators"
generators / normalizedName := "generators"

val org = "com.pixelpapercraft.generator-builder"
val v = "1.0.0-alpha.1"
val scalaV = "3.1.0"
val scalaOpts = Seq(
  "-feature",
  "-deprecation",
  "-Werror"
)

builder / organization := org
generators / organization := org
builder / version := v
generators / version := v
builder / scalaVersion := scalaV
generators / scalaVersion := scalaV
builder / scalacOptions ++= scalaOpts
generators / scalacOptions ++= scalaOpts
builder / libraryDependencies ++= Seq(
  // "org.scala-js" %%% "scalajs-dom" % "2.0.0",
  "com.lihaoyi" %%% "scalatags" % "0.10.0" cross CrossVersion.for3Use2_13
)
generators / libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "scalatags" % "0.10.0" cross CrossVersion.for3Use2_13
)

builder / scalaJSUseMainModuleInitializer := false
generators / scalaJSUseMainModuleInitializer := false
builder / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }
generators / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }

builder / Test / scalaJSUseTestModuleInitializer := false
generators / Test / scalaJSUseTestModuleInitializer := false
builder / Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }
generators / Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }

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

builder / console / scalacOptions ~= filterConsoleScalacOptions
generators / console / scalacOptions ~= filterConsoleScalacOptions
builder / Test / console / scalacOptions ~= filterConsoleScalacOptions
generators / Test / console / scalacOptions ~= filterConsoleScalacOptions