
name := "kafka-training"

version := "1.0"

organization := "com.thoughtworks"

scalaVersion := "2.12.4"

assemblyJarName in assembly := s"de-training-${version.value}.jar"

test in assembly := {}

// allows us to include spark packages
resolvers += "bintray-spark-packages" at
  "https://dl.bintray.com/spark-packages/maven/"

resolvers += "Typesafe Simple Repository" at
  "http://repo.typesafe.com/typesafe/simple/maven-releases/"

resolvers += "MavenRepository" at
  "https://mvnrepository.com/"

resolvers += "Confluent repo" at
  "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  //kafka
  "org.apache.kafka" % "kafka-clients" % "2.1.0",
  "org.apache.kafka" %% "kafka-streams-scala" % "2.1.0",
  "io.confluent" % "kafka-avro-serializer" % "5.0.1",


"javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" artifacts( Artifact("javax.ws.rs-api", "jar", "jar")),

// testing
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",

  // logging
  "org.apache.logging.log4j" % "log4j-api" % "2.4.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.4.1"
)



mainClass in(Compile, packageBin) := Some("com.thoughtworks.Main")

// Compiler settings. Use scalac -X for other options and their description.
// See Here for more info http://www.scala-lang.org/files/archive/nightly/docs/manual/html/scalac.html
scalacOptions ++= List("-feature", "-deprecation", "-unchecked", "-Xlint")

// ScalaTest settings.
// Ignore tests tagged as @Slow (they should be picked only by integration test)
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l",
  "org.scalatest.tags.Slow", "-u", "target/junit-xml-reports", "-oD", "-eS")

