addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.9")

// Resolve scala-xml version conflict between scoverage 2.x and scalastyle
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

libraryDependencies ++= Seq("com.amazonaws" % "aws-java-sdk-s3" % "1.11.202", "org.apache.ivy" % "ivy" % "2.4.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.11")
