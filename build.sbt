val unusedRepo = Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at s"${nexus}content/repositories/snapshots")
    else Some("releases" at s"${nexus}service/local/staging/deploy/maven2")
  },
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
)

val dontPublishSettings = Seq(
  publish := {},
  publishTo := unusedRepo,
  publishArtifact := false
)

val commonSettings = Seq(
  organization := "com.github.fomkin",
  version := "0.6.1",
  scalaVersion := "2.12.4", // Need by IntelliJ
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
    "-Xexperimental"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "utest" % "0.4.5" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
  )
)

lazy val core = crossProject
  .crossType(CrossType.Pure)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "levsha-core",
    libraryDependencies ++= Seq(
      // Macro compat
      "org.typelevel" %% "macro-compat" % "1.1.1" % "provided",
      "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
    )
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val events = crossProject
  .crossType(CrossType.Pure)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(name := "levsha-events")
  .dependsOn(core)

lazy val eventsJS = events.js
lazy val eventsJVM = events.jvm

lazy val dom = project
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .dependsOn(coreJS)
  .dependsOn(eventsJS)
  .settings(
    name := "levsha-dom",
    //scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.3"
    )
  )

lazy val bench = project
  .enablePlugins(JmhPlugin)
  .enablePlugins(SbtTwirl)
  .settings(commonSettings: _*)
  .settings(dontPublishSettings: _*)
  .dependsOn(coreJVM)
  .settings(
    name := "levsha-bench",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "scalatags" % "0.6.5"
    )
  )

lazy val root = project
  .in(file("."))
  .settings(dontPublishSettings:_*  )
  .settings(name := "levsha")
  .aggregate(
    coreJS, coreJVM,
    eventsJS, eventsJVM,
    dom
  )

crossScalaVersions := Seq("2.11.11", "2.12.4")
