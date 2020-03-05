name := "tasbih"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "dev.zio" %% "zio" % "1.0.0-RC17"

//resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
//libraryDependencies += "org.scorexfoundation" %% "scrypto" % "2.1.6"

val tsecV = "0.0.1-M11"
libraryDependencies ++= Seq(
  "io.github.jmcardon" %% "tsec-hash-jca" % tsecV,
  "io.github.jmcardon" %% "tsec-hash-bouncy" % tsecV,
  "com.roundeights" %% "hasher" % "1.2.0",
)

libraryDependencies += "io.monix" %% "monix" % "3.1.0"
libraryDependencies += "io.monix" %% "monix-eval" % "3.1.0"
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC10"
scalacOptions += "-Ypartial-unification"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
