enablePlugins(ScalaNativePlugin)

lazy val sntb = project
  .in(file("sntb"))
  .settings(
    name := "SNTB",
    version := Settings.version,
    organization := "com.simianquant",
    scalaVersion := Settings.scalaVersion,
    crossScalaVersions := Settings.crossScalaVersions,
    nativeMode := "release",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ),
    incOptions := incOptions.value.withLogRecompileOnMacro(false),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
    pomIncludeRepository := { _ =>
      false
    },
    pomExtra := (<url>https://github.com/harshad-deo/sntb</url>
      <licenses>
        <license>
          <name>Apache-2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:git@github.com:harshad-deo/sntb.git</connection>
        <developerConnection>scm:git:git@github.com:harshad-deo/sntb.git</developerConnection>
        <url>git@github.com:harshad-deo/sntb.git</url>
      </scm>
      <developers>
        <developer>
          <id>harshad-deo</id>
          <name>Harshad Deo</name>
          <url>https://github.com/harshad-deo</url>
        </developer>
      </developers>)
  )

lazy val sntbtests = project
  .in(file("sntbtests"))
  .settings(
    name := "SNTB-TESTS",
    version := Settings.version,
    crossScalaVersions := Settings.crossScalaVersions,
    scalaVersion := Settings.scalaVersion,
    nativeMode := "release",
    incOptions := incOptions.value.withLogRecompileOnMacro(false)
  )
  .dependsOn(sntb)
  .aggregate(sntb)

onLoad in Global := (Command.process("project sntbtests", _: State)) compose (onLoad in Global).value
