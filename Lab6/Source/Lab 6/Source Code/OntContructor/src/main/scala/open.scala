/**
  * Created by VenkatNag on 7/18/2017.
  */
import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import com.sun.glass.ui.Window.Level
import com.sun.istack.internal.logging.Logger
import org.apache.log4j.Level
import org.apache.spark.{SparkConf, SparkContext}

object open {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sparkContext = new SparkContext(conf)




    val ipfile = sparkContext.textFile("data/sqad_data").map(s => {
      //Getting OpenIE Form of the word using lda.CoreNLP

      val output = MainNLP.returnTriplets(s)
      output
    })

    //println(CoreNLP.returnTriplets("The dog has a lifespan of upto 10-12 years."))
    println(ipfile.collect().mkString("\n"))
    //writing the OPenIE triplets into a file
    // writer.write(ipfile.collect().mkString("\n"))
    // writer.close()
  }
