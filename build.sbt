import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

ThisBuild / scalacOptions --= Seq("-Werror")

lazy val os = (project in file("."))
  .settings(name := "os")
  .aggregate(osCore.projectRefs *)
  .aggregate(osParse.projectRefs *)

lazy val osCore = (projectMatrix in file("os-core"))
  .settings(name := "os-core")
  .settings(libraryDependencies ++= dependencies(
    peknight.codec,
    fs2.io,
    typelevel.spire,
  ))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))

lazy val osParse = (projectMatrix in file("os-parse"))
  .settings(name := "os-parse")
  .settings(libraryDependencies ++= dependencies(typelevel.catsParse))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
