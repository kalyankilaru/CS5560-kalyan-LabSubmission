import java.io.{File, FileOutputStream, PrintWriter}

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.rdd.RDD

import scala.collection.immutable.HashMap
import scala.io.Source

/**
  * Created by Mayanka on 19-06-2017.
  */
object TF_IDF {
  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "D:\\Mayanka Lenevo F Drive\\winutils")

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    //Reading the Text File where each sentence is a textfile
    val documents = sc.textFile("data/sample1")


   // val documents1 = sc.textFile("data/sample1")
   //   .map(_.split(" ").toSeq)



    //val documen=sc.wholetextfiles("data")in data folder different files are there

    //Getting the Lemmatised form of the words in TextFile
    val documentseq = documents.map(f => {
      val lemmatised = CoreNLP.returnLemma(f) //returnlemma(f,2)where first is path and second is sentence
      val splitString = lemmatised.split(" ")
      splitString.toSeq
    })

      //returnlemma(f,2)where first is path and second is sentence

    val documentseq1 = documents.map(f => {

      val splitString = f.split(" ")
      splitString.toSeq
      // x.toSeq
    })






    //Creating an object of HashingTF Class
    val hashingTF = new HashingTF()

    //Creating Term Frequency of the document
    val tf = hashingTF.transform(documentseq1)//documen
    tf.cache()


    val idf = new IDF().fit(tf)

    //Creating Inverse Document Frequency
    val tfidf = idf.transform(tf)

    val tfidfvalues = tfidf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val values = ff(2).replace("]", "").replace(")", "").split(",")//values
      values
    })

    val tfidfindex = tfidf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val indices = ff(1).replace("]", "").replace(")", "").split(",")//indices
      indices
    })

    tfidf.foreach(f => println(f))

    val tfidfData = tfidfindex.zip(tfidfvalues)

    var hm = new HashMap[String, Double]

    tfidfData.collect().foreach(f => {
      hm += f._1 -> f._2.toDouble
    })

    val mapp = sc.broadcast(hm)

    val documentData = documentseq.flatMap(_.toList)
    val dd = documentData.map(f => {
      val i = hashingTF.indexOf(f)
      val h = mapp.value
      (f, h(i.toString))

    })


    val dd1 = dd.distinct().sortBy(_._2, false)
    dd1.take(20).foreach(f = f => {
      println(f)
      val f2=f._1;
      val write = new PrintWriter(new FileOutputStream(new File("file1"),true))

      write.write(f2)
      write.write("\n")


      write.close()


    })

  }

}
