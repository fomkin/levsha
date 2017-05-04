name := "levsha"

version := "0.1.0"

scalaVersion := "2.11.8" // Need by IntelliJ

organization := "com.github.fomkin"

//testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-verbosity", "3")

libraryDependencies ++= Seq(
  // Macro compat
  "org.typelevel" %% "macro-compat" % "1.1.1",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
  compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  // Test
  "com.lihaoyi" %% "utest" % "0.4.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

testFrameworks += new TestFramework("utest.runner.Framework")

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at s"${nexus}content/repositories/snapshots")
  else Some("releases" at s"${nexus}service/local/staging/deploy/maven2")
}

pomExtra := {
  <url>https://github.com/fomkin/levsha</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:fomkin/levsha.git</url>
    <connection>scm:git:git@github.com:fomkin/levsha.git</connection>
  </scm>
  <developers>
    <developer>
      <id>fomkin</id>
      <name>Aleksey Fomkin</name>
      <email>aleksey.fomkin@gmail.com</email>
    </developer>
  </developers>
}

crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.2")
