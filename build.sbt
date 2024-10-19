ThisBuild / organization := "com.tubitv"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "xfieldmask",
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.17" % "protobuf",
      "com.google.protobuf" % "protobuf-java" % "4.27.1",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )

enablePlugins(ProtocPlugin)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value // ScalaPB generates Scala code from .proto files
)

Compile / PB.protoSources := Seq(sourceDirectory.value / "main" / "protobuf")
