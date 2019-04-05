import xerial.sbt.Sonatype._
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

val unusedRepo = Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

val publishSettings = Seq(
  publishTo := sonatypePublishTo.value,
  publishArtifact in Test := false,
  publishMavenStyle := true,
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  headerLicense := Some(HeaderLicense.ALv2("2017-2019", "Aleksey Fomkin")),
  excludeFilter.in(headerSources) := HiddenFileFilter || "IntStringMap.scala",
  sonatypeProjectHosting := Some(GitHubHosting("fomkin", "levsha", "Aleksey Fomkin", "aleksey.fomkin@gmail.com"))
)

val dontPublishSettings = Seq(
  publish := {},
  publishTo := unusedRepo,
  publishArtifact := false,
  headerLicense := None
)

val commonSettings = Seq(
  organization := "com.github.fomkin",
  crossScalaVersions := Seq("2.11.12", "2.12.8"),
  git.useGitDescribe := true,
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
    "-Xexperimental",
    "-unchecked"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "utest" % "0.6.6" % "test",
    "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
  )
)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(GitVersioning)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    normalizedName := "levsha-core",
    libraryDependencies ++= Seq(
      // Macro compat
      "org.typelevel" %% "macro-compat" % "1.1.1" % "provided",
      "com.lihaoyi" %%% "fastparse" % "1.0.0" % "provided",
      "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
    )
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val events = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(GitVersioning)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(normalizedName := "levsha-events")
  .dependsOn(core)

lazy val eventsJS = events.js
lazy val eventsJVM = events.jvm

lazy val dom = project
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(GitVersioning)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .dependsOn(coreJS)
  .dependsOn(eventsJS)
  .settings(
    normalizedName := "levsha-dom",
    //scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.6"
    )
  )

lazy val bench = project
  .enablePlugins(JmhPlugin)
  .enablePlugins(SbtTwirl)
  .settings(commonSettings: _*)
  .settings(dontPublishSettings: _*)
  .dependsOn(coreJVM)
  .settings(
    normalizedName := "levsha-bench",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "scalatags" % "0.6.7"
    )
  )

lazy val root = project
  .in(file("."))
  .settings(commonSettings:_*)
  .settings(dontPublishSettings:_*  )
  .settings(normalizedName := "levsha")
  .aggregate(
    coreJS, coreJVM,
    eventsJS, eventsJVM,
    dom
  )

// Don't use it for root project
// For some unknown reason `headerLicense := None` doesn't work.
disablePlugins(HeaderPlugin)