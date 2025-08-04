ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.1"

ThisBuild / organization := "com.peknight"

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / publishTo := {
  val nexus = "https://nexus.peknight.com/repository"
  if (isSnapshot.value)
    Some("snapshot" at s"$nexus/maven-snapshots/")
  else
    Some("releases" at s"$nexus/maven-releases/")
}

ThisBuild / credentials ++= Seq(
  Credentials(Path.userHome / ".sbt" / ".credentials")
)

ThisBuild / resolvers ++= Seq(
  "Pek Nexus" at "https://nexus.peknight.com/repository/maven-public/",
)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    "-Xmax-inlines:64"
  ),
)

lazy val os = (project in file("."))
  .aggregate(
    osCore.jvm,
    osCore.js,
    osFs2.jvm,
    osFs2.js,
    osParse.jvm,
    osParse.js,
  )
  .settings(commonSettings)
  .settings(
    name := "os",
  )

lazy val osCore = (crossProject(JSPlatform, JVMPlatform) in file("os-core"))
  .settings(commonSettings)
  .settings(
    name := "os-core",
    libraryDependencies ++= Seq(
      "com.peknight" %%% "error-core" % pekErrorVersion,
    ),
  )

lazy val osFs2 = (crossProject(JSPlatform, JVMPlatform) in file("os-fs2"))
  .dependsOn(osCore)
  .settings(commonSettings)
  .settings(
    name := "os-fs2",
    libraryDependencies ++= Seq(
      "co.fs2" %%% "fs2-io" % fs2Version,
    ),
  )

lazy val osParse = (crossProject(JSPlatform, JVMPlatform) in file("os-parse"))
  .settings(commonSettings)
  .settings(
    name := "os-parse",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-parse" % catsParseVersion,
    ),
  )

val fs2Version = "3.12.0"
val catsParseVersion = "0.3.10"
val pekVersion = "0.1.0-SNAPSHOT"
val pekErrorVersion = pekVersion
