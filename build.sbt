
lazy val `codetools-core` =
  project

lazy val codetools =
  project
    .dependsOn(`codetools-core`)

lazy val `codetools-play_json` =
  project
    .dependsOn(codetools)