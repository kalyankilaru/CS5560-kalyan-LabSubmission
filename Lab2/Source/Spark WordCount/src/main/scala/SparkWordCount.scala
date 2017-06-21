

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by putha on 6/14/2017
 */
object SparkWordCount {

  def main(args: Array[String]) {

    System.setProperty("home.hadoop.dir","C:\\Users\\putha\\Desktop\\KDM\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)

    //val input=sc.textFile("src//main//InPutFile//ParisAttacks2015.txt")
    val input=sc.textFile("input");

    val wc=input.flatMap(line=>{line.split(" ")}).map(word=>(word,1)).cache()

    val output=wc.reduceByKey(_+_)

    output.saveAsTextFile("src//main//OuTputFile//output")

    val o=output.collect()

    var s:String="Words:Count \n"
    o.foreach{case(word,count)=>{

      s+=word+" : "+count+"\n"

    }}

  }

}
