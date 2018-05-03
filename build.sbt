name := "projectOCR"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.2.0"
val hbaseVersion = "1.1.2"
val tessVersion  = "3.4.2"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.hbase" % "hbase-client" % hbaseVersion,
  "org.apache.hbase" % "hbase-common" % hbaseVersion,
  "net.sourceforge.tess4j" % "tess4j" % tessVersion
)