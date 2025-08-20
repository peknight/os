import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val os = (project in file("."))
  .settings(name := "os")
  .aggregate(
    osCore.jvm,
    osCore.js,
    osFs2.jvm,
    osFs2.js,
    osParse.jvm,
    osParse.js,
  )

lazy val osCore = (crossProject(JVMPlatform, JSPlatform) in file("os-core"))
  .settings(name := "os-core")
  .settings(crossDependencies(
    peknight.codec,
    typelevel.spire,
  ))

lazy val osFs2 = (crossProject(JVMPlatform, JSPlatform) in file("os-fs2"))
  .dependsOn(osCore)
  .settings(name := "os-fs2")
  .settings(crossDependencies(fs2.io))

lazy val osParse = (crossProject(JVMPlatform, JSPlatform) in file("os-parse"))
  .settings(name := "os-parse")
  .settings(crossDependencies(typelevel.catsParse))
