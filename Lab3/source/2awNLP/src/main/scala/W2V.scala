import java.io.File
import scala.io.Source


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}
import java.util



object W2V {
  def main(args: Array[String]): Unit = {

    //System.setProperty("hadoop.home.dir", "D:\\Mayanka Lenevo F Drive\\winutils")

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
      .set("spark.driver.memory", "6g").set("spark.executor.memory", "6g")


    val sc = new SparkContext(sparkConf)
    val lines = new util.ArrayList[String]
    var z = new Array[String](50)




    var myList = Array("Obama", "Hawaii")

      val input = sc.textFile("data/sample1").map(line => line.split(" ").toSeq)

      val modelFolder = new File("myModelPath")
      import java.util


      if (modelFolder.exists()) {
        val sameModel = Word2VecModel.load(sc, "myModelPath")

        for (x <- myList) {
          println(x)


          val synonyms = sameModel.findSynonyms(x, 10)

          for ((synonym, cosineSimilarity) <- synonyms) {
            println(s"$synonym $cosineSimilarity")
          }
        }
      }

      else {
        val word2vec = new Word2Vec().setVectorSize(1000)

        val model = word2vec.fit(input)

        val synonyms = model.findSynonyms("Obama", 10)

        for ((synonym, cosineSimilarity) <- synonyms) {
          println(s"$synonym $cosineSimilarity")
        }

        model.getVectors.foreach(f => println(f._1 + ":" + f._2.length))

        // Save and load model
        model.save(sc, "myModelPath")

      }

    }
  }
