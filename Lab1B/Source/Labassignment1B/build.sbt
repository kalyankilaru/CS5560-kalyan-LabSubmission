name := "Labassignment1B"

version := "1.0"

scalaVersion := "2.12.1"


libraryDependencies ++= Seq("edu.stanford.nlp" % "stanford-corenlp" % "3.3.0",

  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.0" classifier "models",

  "edu.stanford.nlp" % "stanford-parser" % "3.3.0",

  "com.google.protobuf" % "protobuf-java" % "2.6.1"

)