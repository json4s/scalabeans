import scala.xml.Group

organization := "org.json4s"

name := "scalabeans"

version := "0.3.0-SNAPSHOT"

scalaVersion := "2.9.2"

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

scalacOptions ++= Seq("-optimize", "-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8")

homepage := Some(url("https://github.com/scalastuff/scalabeans"))

startYear := Some(2010)

licenses := Seq(("ASL", url("http://www.apache.org/licenses/LICENSE-2.0.txt")))

pomExtra <<= (pomExtra, name, description) {(pom, name, desc) => pom ++ Group(
  <scm>
		<connection>scm:git:git@github.com:scalastuff/scalabeans.git</connection>
		<url>https://github.com/scalastuff/scalabeans</url>
	</scm>
	<developers>
		<developer>
			<id>rditerwich</id>
			<name>Ruud Diterwich</name>
		</developer>
		<developer>
			<id>advorkovyy</id>
			<name>Alexander Dvorkovyy</name>
		</developer>
	</developers>
	<contributors>
		<contributor>
			<name>Sergey Nazarov</name>
		</contributor>
	</contributors>
)}

packageOptions <+= (name, version, organization) map {
    (title, version, vendor) =>
      Package.ManifestAttributes(
        "Created-By" -> "Simple Build Tool",
        "Built-By" -> System.getProperty("user.name"),
        "Build-Jdk" -> System.getProperty("java.version"),
        "Specification-Title" -> title,
        "Specification-Version" -> version,
        "Specification-Vendor" -> vendor,
        "Implementation-Title" -> title,
        "Implementation-Version" -> version,
        "Implementation-Vendor-Id" -> vendor,
        "Implementation-Vendor" -> vendor
      )
  }

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { x => false }

libraryDependencies ++= Seq(
  "com.thoughtworks.paranamer" % "paranamer" % "2.5.1",
  "org.codehaus.woodstox" % "woodstox-core-asl" % "4.1.4" % "optional",
  "junit" % "junit" % "4.8.2" % "test",
  "com.google.guava" % "guava" % "13.0.1",
  "com.dyuproject.protostuff" % "protostuff-core" % "1.0.7" % "optional",
  "com.dyuproject.protostuff" % "protostuff-api" % "1.0.7" % "optional",
  "com.dyuproject.protostuff" % "protostuff-json" % "1.0.7" % "optional",
  "com.dyuproject.protostuff" % "protostuff-xml" % "1.0.7" % "optional"
)
