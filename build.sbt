import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val os = (project in file("."))
  .settings(name := "os")
  .aggregate(
    osCore.jvm,
    osCore.js,
    osParse.jvm,
    osParse.js,
  )

lazy val osCore = (crossProject(JVMPlatform, JSPlatform) in file("os-core"))
  .settings(name := "os-core")
  .settings(crossDependencies(
    peknight.codec,
    fs2.io,
    typelevel.spire,
  ))

lazy val osParse = (crossProject(JVMPlatform, JSPlatform) in file("os-parse"))
  .settings(name := "os-parse")
  .settings(crossDependencies(typelevel.catsParse))
