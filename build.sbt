import xerial.sbt.Sonatype._
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

val unusedRepo = Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

val publishSettings = Seq(
  publishTo := sonatypePublishTo.value,
  publishArtifact in Test := false,
  publishMavenStyle := true,
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  headerLicense := Some(HeaderLicense.ALv2("2017-2020", "Aleksey Fomkin")),
  excludeFilter.in(headerSources) := HiddenFileFilter || "IntStringMap.scala" || "StringSet.scala",
  sonatypeProjectHosting := Some(GitHubHosting("fomkin", "levsha", "Aleksey Fomkin", "aleksey.fomkin@gmail.com"))
)

val dontPublishSettings = Seq(
  publish := {},
  publishTo := unusedRepo,
  publishArtifact := false,
  headerLicense := None
)

def additionalUnmanagedSources(cfg: Configuration) = Def.setting {
  val isCrossProject = baseDirectory.value.getName match {
    case ".js" => true
    case ".jvm" => true
    case ".native" => true
    case _ => false
  }
  val origSourceDir = (cfg / sourceDirectory).value
  val sourceDir =
    if (isCrossProject) file((cfg / baseDirectory).value.getParent) / "src" / origSourceDir.getName
    else origSourceDir
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _))  => Seq(sourceDir / "scala-3")
    case Some((2, _))  => Seq(sourceDir / "scala-2")
    case _             => Seq()
  }
}

val crossVersionSettings = Seq(
  crossScalaVersions := Seq("2.13.6", "2.12.11", "3.0.1"),
  scalaVersion := "3.0.1"
)

val commonSettings = Seq(
  scalaVersion := "2.13.6",
  organization := "com.github.fomkin",
  git.useGitDescribe := true,
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "utest" % "0.7.10" % "test",
    "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"
  ),
  // Add some more source directories
  unmanagedSourceDirectories in Compile ++= additionalUnmanagedSources(Compile).value,
  unmanagedSourceDirectories in Test ++= additionalUnmanagedSources(Test).value
)


lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(GitVersioning)
  .enablePlugins(HeaderPlugin)
  .settings(commonSettings: _*)
  .settings(crossVersionSettings:_*)
  .settings(publishSettings: _*)
  .settings(
    normalizedName := "levsha-core",
    libraryDependencies ++= (
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, _)) =>
          List("org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided")
        case _                       => Nil
      }
    )
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val events = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(GitVersioning)
  .enablePlugins(HeaderPlugin)
  .settings(commonSettings: _*)
  .settings(crossVersionSettings:_*)
  .settings(publishSettings: _*)
  .settings(normalizedName := "levsha-events")
  .dependsOn(core)

lazy val eventsJS = events.js
lazy val eventsJVM = events.jvm

lazy val dom = project
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(GitVersioning)
  .enablePlugins(HeaderPlugin)
  .settings(commonSettings: _*)
  .settings(crossVersionSettings:_*)
  .settings(publishSettings: _*)
  .dependsOn(coreJS)
  .dependsOn(eventsJS)
  .settings(
    normalizedName := "levsha-dom",
    //scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % "1.1.0").cross(CrossVersion.for3Use2_13)
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
      ("com.lihaoyi" %% "scalatags" % "0.9.4").cross(CrossVersion.for3Use2_13)
    )
  )

lazy val root = project
  .in(file("."))
  .settings(commonSettings:_*)
  .settings(crossVersionSettings:_*)
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
