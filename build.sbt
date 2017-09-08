enablePlugins(ScalaNativePlugin)

lazy val root = project
  .in(file("."))
  .settings(
    name := "sntb",
    version := "0.3.2-SNAPSHOT",
    organization := "com.simianquant",
    scalaVersion := "2.11.11",
    nativeMode := "release",
    libraryDependencies ++= Seq(
      "org.scala-native" % "test-interface_native0.3_2.11" % "0.3.3",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ),
    testFrameworks += new TestFramework("sntb.SntbFramework"),
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