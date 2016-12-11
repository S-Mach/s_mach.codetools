// needed to ignore warnings on A.discard
scalacOptions += "-Xlint:-nullary-unit,_"

libraryDependencies ++= Seq(
  "net.s_mach" %% "string" % "2.0.1"
)
