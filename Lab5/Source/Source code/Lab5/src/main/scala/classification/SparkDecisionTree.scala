/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package classification

import java.io.PrintStream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel, RegexTokenizer, StopWordsRemover}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import scopt.OptionParser

import scala.collection.immutable.HashMap

object SparkDecisionTree {

  private case class Params(
                             input: Seq[String] = Seq.empty
                           )

  def main(args: Array[String]) {
    /*val defaultParams = Params()

    val parser = new OptionParser[Params]("KMeansExample") {
      head("KMeansExample: an example KMeans app for plain text data.")
      arg[String]("<input>...")
        .text("input paths (directories) to plain text corpora." +
          "  Each text file line should hold 1 document.")
        .unbounded()
        .required()
        .action((x, c) => c.copy(input = c.input :+ x))
    }

    parser.parse(args, defaultParams).map { params =>
      run(params)
    }.getOrElse {
      parser.showUsageAsError
      sys.exit(1)
    }
  }

  private def run(params: Params) {*/
    System.setProperty("hadoop.home.dir", "E:\\UMKC\\Sum_May\\KDM\\winutils")
    val conf = new SparkConf().setAppName(s"KMeansExample with ").setMaster("local[*]").set("spark.driver.memory", "4g").set("spark.executor.memory", "4g")
    val sc = new SparkContext(conf)
    val  input="E:\\UMKC\\Sum_May\\KDM\\Week 5\\Yahoo-Questions-traindata.csv"
    val test="E:\\UMKC\\Sum_May\\KDM\\Week 5\\Yahoo-Question-testdata.csv"
    Logger.getRootLogger.setLevel(Level.WARN)

    val topic_output = new PrintStream("data/Decision_Results.txt")
    // Load documents, and prepare them for KMeans.
    val preprocessStart = System.nanoTime()


    val (inputVector, corpusData, vocabArray) =
      preprocess(sc, input)
    val data = corpusData.zip(inputVector)
   /* var hm = new HashMap[String, Int]()
    val IMAGE_CATEGORIES = List("athletics", "cricket", "football", "rugby","tennis")
    var index = 0
    IMAGE_CATEGORIES.foreach(f => {
      hm += IMAGE_CATEGORIES(index) -> index
      index += 1
    })
    val mapping = sc.broadcast(hm)*/

    val featureVector = data.map(f => {
  //    val location_array = f._1._1.split("/")
    //  val class_name = location_array(location_array.length - 2)

      new LabeledPoint(f._1._1.toString.toDouble, f._2)
    })
  /*  val splits = featureVector.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0)
    val test = splits(1)*/
    featureVector.foreach(println)
 val IMAGE_CATEGORIES = List("1", "2", "3", "4","5","6","7")
    val training=featureVector
   val numClasses = IMAGE_CATEGORIES.length
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainClassifier(training, numClasses+1, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)
    val (testvector,testdata,testvoc)=preprocess(sc,test)
    val tdata=testdata.zip(testvector)
    val testfeaturevector=tdata.map(f=>{new LabeledPoint(f._1._1.toString.toDouble,f._2)})
    val test_vector=tdata.map(f=>{f._2})
    val predict=testfeaturevector.map(f=>(model.predict(f.features),f.label))
    val x=for((e,a) <- (predict zip testdata)) yield (a._2,e._1)
    val y=x.map(f=>(f._1,
      if(f._2==1.0) "Business&Finance"
      else if(f._2==2.0) "Computers&Internet"
      else if(f._2==3.0) "Entertainment&Music"
      else if(f._2==4.0) "Family&Relationships"
      else if(f._2==5.0) "Education&Reference"
      else if(f._2==6.0) "Health"
      else if(f._2==7.0) "Science&Mathematics"))
    y.foreach(println)
   // val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))


    val accuracy = 1.0 * predict.filter(x => x._1 == x._2).count() / testdata.count()

    val metrics = new MulticlassMetrics(predict)

    // Confusion matrix
    topic_output.println("Confusion matrix:")
    topic_output.println(metrics.confusionMatrix)

    topic_output.println("Accuracy: " + accuracy)


    sc.stop()
  }
  /**
    * Load documents, tokenize them, create vocabulary, and prepare documents as term count vectors.
    *
    * @return (corpus, vocabulary as array, total token count in corpus)
    */
  private def preprocess(sc: SparkContext,paths: String): (RDD[Vector], RDD[(String,String)], Long) = {

    //Reading Stop Words
    val stopWords=sc.textFile("data/stopwords.txt").collect()
    val stopWordsBroadCast=sc.broadcast(stopWords)

    val df1 = sc.textFile(paths)
    val df=df1.map(f => f.split(",")).map(f=>{
   //   val lemma=CoreNLP.returnLemma(f(1))
      val splitString = f(1).split(" ")
      (f(0),splitString)
    })


    val stopWordRemovedDF=df.map(f=>{
      //Filtered numeric and special characters out
      val filteredF=f._2.map(_.replaceAll("[^a-zA-Z]",""))
        //Filter out the Stop Words
        .filter(ff=>{
        if(stopWordsBroadCast.value.contains(ff.toLowerCase))
          false
        else
          true
      })
      (f._1,filteredF)
    })

    val data=stopWordRemovedDF.map(f=>{(f._1,f._2.mkString(" "))})
    val dfseq=stopWordRemovedDF.map(_._2.toSeq)

    //Creating an object of HashingTF Class
    val hashingTF = new HashingTF(stopWordRemovedDF.count().toInt)  // VectorSize as the Size of the Vocab

    //Creating Term Frequency of the document
    val tf = hashingTF.transform(dfseq)
    tf.cache()

    val idf = new IDF().fit(tf)
    //Creating Inverse Document Frequency
    val tfidf1 = idf.transform(tf)
    tfidf1.cache()



    val dff= stopWordRemovedDF.flatMap(f=>f._2)
    val vocab=dff.distinct().collect()
    (tfidf1, data, dff.count()) // Vector, Data, total token count
  }

}

// scalastyle:on println
