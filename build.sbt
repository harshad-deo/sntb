enablePlugins(ScalaNativePlugin)

lazy val sntb = project
  .in(file("sntb"))
  .settings(
    name := "SNTB",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    nativeMode := "release",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ),
    incOptions := incOptions.value.withLogRecompileOnMacro(false)
  )

lazy val sntbtests = project
  .in(file("sntbtests"))
  .settings(
    name := "SNTB-TESTS",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    nativeMode := "release",
    incOptions := incOptions.value.withLogRecompileOnMacro(false)
  )
  .dependsOn(sntb)
  .aggregate(sntb)

onLoad in Global := (Command.process("project sntbtests", _: State)) compose (onLoad in Global).value
