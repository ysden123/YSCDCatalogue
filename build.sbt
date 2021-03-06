lazy val root = (project in file(".")).
  settings(
    name := "YSCDCatalogue",
    version := "2.1.1",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
    	"org.apache.commons" % "commons-lang3" % "3.4"
    	, "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.4"
    	, "org.apache.logging.log4j" % "log4j-api" % "2.5"
    	, "org.apache.logging.log4j" % "log4j-core" % "2.5"
    	, "org.mapdb" % "mapdb" % "1.0.8"
    	, "junit" % "junit" % "4.12"
    ),
    scalacOptions in (Compile, doc) ++= Seq("-author"),
    packAutoSettings,
    packExcludeJars := Seq(
    	"junit-4.12.jar"
		)
)