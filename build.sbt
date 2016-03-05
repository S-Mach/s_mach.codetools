val nexus = "https://oss.sonatype.org/"
val nexusSnapshots = "snapshots" at nexus + "content/repositories/snapshots"
val nexusReleases = "releases"  at nexus + "service/local/staging/deploy/maven2"

// TODO: figure out how to move this into publish.sbt
val publishSettings = Seq(
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  publishTo := {
    if (isSnapshot.value)
      Some(nexusSnapshots)
    else
      Some(nexusReleases)
  },
  pomExtra :=
    <url>https://github.com/S-Mach/s_mach.codetools</url>
      <licenses>
        <license>
          <name>MIT</name>
          <url>http://opensource.org/licenses/MIT</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:S-Mach/s_mach.codetools.git</url>
        <connection>scm:git:git@github.com:S-Mach/s_mach.codetools.git</connection>
        <developerConnection>scm:git:git@github.com:S-Mach/s_mach.codetools.git</developerConnection>
      </scm>
      <developers>
        <developer>
          <id>lancegatlin</id>
          <name>Lance Gatlin</name>
          <email>lance.gatlin@gmail.com</email>
          <organization>S-Mach</organization>
          <organizationUrl>http://s-mach.net</organizationUrl>
        </developer>
      </developers>
)

val defaultSettings = Defaults.coreDefaultSettings ++ publishSettings ++ Seq(
  scalaVersion := "2.11.7",
  organization := "net.s_mach",
  version := "1.1.0",
  scalacOptions ++= Seq(
    "-feature",
    "-unchecked",
    "-deprecation"
  ),
  // TODO: remove this once s_mach.codetools is off snapshot version
  resolvers += nexusSnapshots
)

val test = Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test"
)

lazy val codetools = Project(
  id = "codetools",
  base = file("."),
  aggregate = Seq(
    codetoolsCore
  ),
  dependencies = Seq("codetools-core")
)
  .settings(defaultSettings: _*)
  .settings(libraryDependencies ++= test)

lazy val codetoolsCore = Project(
  id = "codetools-core",
  base = file("codetools-core")
)
  .settings(defaultSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ))
