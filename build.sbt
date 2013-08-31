sbtPlugin := true

name := "sbt-sound"

organization := "com.orrsella"

version := "1.0.5-SNAPSHOT"

// scalaVersion in Global := "2.10.2"
// scalacOptions ++= Seq("-feature")

// publishing
crossScalaVersions <<= sbtVersion { ver =>
  ver match {
    case "0.12.4" => Seq("2.9.0", "2.9.1", "2.9.2", "2.9.3", "2.10.2")
    case "0.13.0" => Seq("2.10.2")
    case _ => sys.error("Unknown sbt version")
  }
}

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/orrsella/sbt-sound</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:orrsella/sbt-sound.git</url>
    <connection>scm:git:git@github.com:orrsella/sbt-sound.git</connection>
  </scm>
  <developers>
    <developer>
      <id>orrsella</id>
      <name>Orr Sella</name>
      <url>http://orrsella.com</url>
    </developer>
  </developers>
)