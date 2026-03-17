ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val scala212 = "2.12.18"
lazy val supportedScalaVersions = List(scala212)
ThisBuild / scalaVersion := scala212

javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = (project in file("."))
  .settings(
    name := "data-loader",
    crossScalaVersions := supportedScalaVersions,
    assembly / test := {},
    
    libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.20.0",
    libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.20.0",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.9",  // Upgraded for Spark 3.4
    libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.20.0",  // Use slf4j2-impl for SLF4J 2.x
    libraryDependencies += ("ch.cern.sparkmeasure" %% "spark-measure" % "0.23" % "provided")
      .excludeAll(
        ExclusionRule(organization = "org.apache.spark", name = "spark-sql_2.12"),
        ExclusionRule(organization = "com.fasterxml.jackson.module", name = "jackson-module-scala")
      ),
      libraryDependencies ++= base_dependencies_group,
    libraryDependencies ++= dependencies_group_2_12,
      assembly / assemblyMergeStrategy := {
      case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
      case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
      case "log4j.properties" => MergeStrategy.discard
      case m if m.toLowerCase.startsWith("meta-inf/services/") =>
        MergeStrategy.filterDistinctLines
      case "reference.conf" => MergeStrategy.concat
      case _ => MergeStrategy.first
    }
    ,
    Compile / assembly / artifact := {
      val art = (Compile / assembly / artifact).value
      art.withClassifier(Some("assembly"))
    }
    ,
    addArtifact(Compile / assembly / artifact, assembly)
    ,
    Test / parallelExecution := false
    ,
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
      Tests.Argument(TestFrameworks.ScalaTest, "-oD")
    )
    ,
    Test / fork := true
    ,
    // Run IcebergCommitterTest in a separate forked JVM to prevent Hive metastore
    // configuration pollution to other tests. This is necessary because TestHiveMetastore
    // sets spark.hadoop.hive.metastore.uris which is cached at JVM level and affects
    // subsequent tests like S3SourceIncrementalConsumerTest, CatalogDriverTest, etc.
    Test / testGrouping := {
      val tests = (Test / definedTests).value
      val icebergTests = tests.filter(_.name.contains("IcebergCommitterTest"))
      val otherTests = tests.filterNot(_.name.contains("IcebergCommitterTest"))
      
      val forkOptions = ForkOptions()
        .withRunJVMOptions((Test / javaOptions).value.toVector)
        .withWorkingDirectory(Some(baseDirectory.value))
      
      Seq(
        // Run IcebergCommitterTest in its own isolated JVM
        Tests.Group(
          name = "IcebergCommitterTest",
          tests = icebergTests,
          runPolicy = Tests.SubProcess(forkOptions)
        ),
        // Run all other tests in a separate JVM
        Tests.Group(
          name = "OtherTests",
          tests = otherTests,
          runPolicy = Tests.SubProcess(forkOptions)
        )
      )
    }
    ,
    Test / javaOptions ++= Seq(
      // Java 25 module access required for Spark 3.4 + Hive + Iceberg + Mockito compatibility.
      // They are needed because:
      // - Spark internals use reflection and sun.misc.Unsafe for performance
      // - Hive metastore uses reflection for serialization
      // - Mockito/ByteBuddy needs deep reflection access for mocking
      "--add-opens=java.base/java.lang=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
      "--add-opens=java.base/java.io=ALL-UNNAMED",
      "--add-opens=java.base/java.net=ALL-UNNAMED",
      "--add-opens=java.base/java.nio=ALL-UNNAMED",
      "--add-opens=java.base/java.util=ALL-UNNAMED",
      "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
      "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
      "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED",
      "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
      "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED",
      "--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED",
      // Additional opens for Hive/Iceberg metastore serialization
      "--add-opens=java.base/java.time=ALL-UNNAMED",
      "--add-opens=java.base/java.text=ALL-UNNAMED",
      "--add-opens=java.base/java.util.regex=ALL-UNNAMED",
      "--add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED",
      "--add-opens=java.base/java.math=ALL-UNNAMED",
      // ByteBuddy/Mockito Java 25 instrumentation support
      "--add-opens=java.base/java.lang.ref=ALL-UNNAMED",
      "--add-opens=java.base/java.security=ALL-UNNAMED",
      // Exports for Spark internals
      "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-exports=java.base/sun.security.util=ALL-UNNAMED",
      "--add-exports=java.base/sun.nio.cs=ALL-UNNAMED",
      // Reflection and serialization settings
      "-Djdk.reflect.useDirectMethodHandle=false",
      "-Dio.netty.tryReflectionSetAccessible=true",
      // ByteBuddy experimental features for Java 25+
      "-Dnet.bytebuddy.experimental=true",
      "-Xmx2g"
    )
    ,
    Test / scalastyleConfig := baseDirectory.value / "scalastyle-test-config.xml"
  )
lazy val base_dependencies_group = Seq(
  base_dependencies.mysqlConnector,
  base_dependencies.postgresConnector,
  base_dependencies.sqlserverConnector,
  base_dependencies.oracleConnector,
  base_dependencies.oraclePki,
  base_dependencies.osdtCore,
  base_dependencies.osdtCert,
  base_dependencies.scalatest,
  base_dependencies.scalaMock,
  base_dependencies.mariadb,
  base_dependencies.mockioScala,
  base_dependencies.mockitoCore,
  base_dependencies.byteBuddy,
  base_dependencies.byteBuddyAgent,
  base_dependencies.scalaCompat,
  base_dependencies.junit,
  base_dependencies.h2
)
lazy val dependencies_group_2_12 = Seq(
  dependencies_2_12.awsS3Sdk,
  dependencies_2_12.awsSdk,
  dependencies_2_12.gson,
  dependencies_2_12.mariadb4j,
  dependencies_2_12.elasticsearch,
  dependencies_2_12.playJson,
  dependencies_2_12.scopt,
  dependencies_2_12_override.sparkSql,
  dependencies_2_12_override.sparkCore,
  dependencies_2_12_override.sparkHive,
  dependencies_2_12_override.sparkStreaming,
  dependencies_2_12_override.sparkAvro,
  dependencies_2_12_override.log4j1,
  dependencies_2_12_override.log4jImpl,
  dependencies_2_12_override.log4jCore,
  dependencies_2_12_override.slfjAPI,
  dependencies_2_12_override.dynamodb,
  dependencies_2_12_override.icebergSpark,
  dependencies_2_12_override.icebergHiveMetastoreTests,
  // Add compatible Jackson versions
  dependencies_2_12_override.jacksonCore,
  dependencies_2_12_override.jacksonDatabind,
  dependencies_2_12_override.jacksonAnnotations,
  dependencies_2_12_override.jacksonScala
//  dependencies_2_12_override.dynamodbHive
)
lazy val base_dependencies =
  new {
    val mysqlConnectorV = "5.1.46"
    val scalatestV = "3.2.0"
    val scalaMockV = "4.4.0"
    val sqlserverConnectorV = "6.1.0.jre8"
    val mariadbV = "2.6.2"
    val postgresConnectorV = "42.2.14"
    val oracleConnectorV = "23.9.0.25.07"
    val oraclePkiV = "23.9.0.25.07"
    val osdtCoreV = "21.19.0.0"
    val osdtCertV = "21.19.0.0"
    // Upgraded mockito-scala for Java 25 compatibility (requires ByteBuddy 1.12+)
    val mockioScalaV = "1.17.14"
    val mockitoCoreV = "4.11.0"
    val byteBuddyV = "1.14.11"
    val scalaCompatV = "2.11.0"
    val junitV = "4.13.2"
    val mysqlConnector = "mysql" % "mysql-connector-java" % mysqlConnectorV
    val sqlserverConnector = "com.microsoft.sqlserver" % "mssql-jdbc" % sqlserverConnectorV
    val mariadb = "org.mariadb.jdbc" % "mariadb-java-client" % mariadbV
    val scalatest = "org.scalatest" %% "scalatest" % scalatestV % Test
    val scalaMock = "org.scalamock" %% "scalamock" % scalaMockV % Test
    val postgresConnector = "org.postgresql" % "postgresql" % postgresConnectorV
    val oracleConnector = "com.oracle.database.jdbc" % "ojdbc8" % oracleConnectorV
    val oraclePki = "com.oracle.database.security" % "oraclepki" % oraclePkiV
    val osdtCore = "com.oracle.database.security" % "osdt_core" % osdtCoreV
    val osdtCert = "com.oracle.database.security" % "osdt_cert" % osdtCertV
    // mockito-scala with explicit mockito-core and ByteBuddy for Java 25 support
    val mockioScala = ("org.mockito" %% "mockito-scala" % mockioScalaV % Test)
      .exclude("org.mockito", "mockito-core")
      .exclude("net.bytebuddy", "byte-buddy")
      .exclude("net.bytebuddy", "byte-buddy-agent")
    val mockitoCore = "org.mockito" % "mockito-core" % mockitoCoreV % Test
    val byteBuddy = "net.bytebuddy" % "byte-buddy" % byteBuddyV % Test
    val byteBuddyAgent = "net.bytebuddy" % "byte-buddy-agent" % byteBuddyV % Test
    val scalaCompat = "org.scala-lang.modules" %% "scala-collection-compat" % scalaCompatV % Test
    val junit = "junit" % "junit" % junitV % Test
    val h2 = "com.h2database" % "h2" % "2.2.224" % Test
  }
lazy val dependencies_2_12_override=
  new {
//    exclude("org.apache.logging.log4j","log4j-slf4j-impl")
    val sparkV = "3.4.1"
    val slfjV="2.0.9"  // Upgraded for Spark 3.4 + Log4j 2.x compatibility
    val log4j2V="2.20.0"  // Upgraded for SLF4J 2.x support (log4j-slf4j2-impl requires 2.19+)
    val icebergV="1.4.3"
    val jacksonV="2.14.2"
    val log4j1="org.apache.logging.log4j" % "log4j-1.2-api" % log4j2V
    val sparkCore = ("org.apache.spark" %% "spark-core" % sparkV % "provided")
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    val sparkStreaming = ("org.apache.spark" %% "spark-streaming" % sparkV % "provided")
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    val sparkSql = ("org.apache.spark" %% "spark-sql" % sparkV)
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    val sparkHive = ("org.apache.spark" %% "spark-hive" % sparkV % "provided")
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    val log4jAPI="org.apache.logging.log4j" % "log4j-api" % log4j2V
    val log4jCore="org.apache.logging.log4j" % "log4j-core" % log4j2V
    val slfjAPI="org.slf4j" % "slf4j-api" % slfjV
    val log4jImpl="org.apache.logging.log4j" % "log4j-slf4j2-impl" % log4j2V  // Use slf4j2-impl for SLF4J 2.x
    val sparkAvro = ("org.apache.spark" %% "spark-avro" % sparkV)
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    val dynamodb = "com.audienceproject" % "spark-dynamodb_2.12" % "1.1.2"
    val icebergSpark = ("org.apache.iceberg" %% "iceberg-spark-runtime-3.4" % icebergV % Test)
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    val icebergHiveMetastoreTests = ("org.apache.iceberg" % "iceberg-hive-metastore" % icebergV % Test classifier "tests")
      .excludeAll(
        ExclusionRule(organization = "com.fasterxml.jackson.core"),
        ExclusionRule(organization = "com.fasterxml.jackson.databind"),
        ExclusionRule(organization = "com.fasterxml.jackson.module")
      )
    // Add compatible Jackson versions
    val jacksonCore = "com.fasterxml.jackson.core" % "jackson-core" % jacksonV
    val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % jacksonV
    val jacksonAnnotations = "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonV
    val jacksonScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonV
//    val dynamodbHive= "com.amazon.emr" % "emr-dynamodb-hive" % "4.16.0"
  }
lazy val dependencies_2_12 =
  new {
    val awsSdkV = "1.11.475"
    val elasticsearchV = "7.12.1"
    val awsSdk = "com.amazonaws" % "aws-java-sdk-sts" % awsSdkV
    val awsS3Sdk = "com.amazonaws" % "aws-java-sdk-s3" % awsSdkV
    val gson = "com.google.code.gson" % "gson" % "2.8.7"
    val elasticsearch = "org.elasticsearch" %% "elasticsearch-spark-30" % elasticsearchV
    val playJson = "com.typesafe.play" %% "play-json" % "2.8.1"
    val scopt = "com.github.scopt" %% "scopt" % "3.7.1"
    val mariadb4j = "ch.vorburger.mariaDB4j" % "mariaDB4j" % "2.4.0" % Test
  }

// release

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

val ver = sys.env.getOrElse("RELEASE_VERSION", null)

releaseIgnoreUntrackedFiles := true
releaseCommitMessage := s"[skip ci] Setting version to ${(ThisBuild / version).value}"
releaseVersionBump := sbtrelease.Version.Bump.Minor
version := {
  if (ver != null && ver.matches("[0-9]+.[0-9]+.[0-9]+")) {
    ver.toString
  } else (ThisBuild / version).value
}

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion, 
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)