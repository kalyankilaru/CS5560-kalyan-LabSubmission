

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by puth on 20-Jun-17.
  */
object SparkWordCount {

  def main(args: Array[String]) {

    System.setProperty("hadoopLocal.homeroot.dirurl", "C:\\Users\\putha\\Desktop\\KDM\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)


    val inputsuji=sc.textFile("input1")

/*
    val wc=input.flatMap(line=>{line.split(" ")}).map(word=>(word,1)).cache()

    val output=wc.reduceByKey(_+_)

    output.saveAsTextFile("output")

    val o=output.collect()

    var s:String="Words:Count \n"
    o.foreach{case(word,count)=>{

      s+=word+" : "+count+"\n"

    }}
*/

    val input1=sc.textFile("input1");
    val input2=sc.textFile("input2");
    val unionSet = input1.union(input2);
    val intersectionSet = input1.intersection(input2);
    val distinctSet = input1.distinct(2);
    var KeyArr = new Array[String](50)
    KeyArr = unionSet.take(30)
    unionSet.saveAsTextFile("union");
    println(unionSet.count())
    println("Union")
    for(s <- KeyArr){
      println(s)
    }
    var KeyArr1 = new Array[String](50)
    KeyArr1 = intersectionSet.take(30)
    intersectionSet.saveAsTextFile("intersection");
    println(intersectionSet.count())
    println("Intersection")
    for(s <- KeyArr1){
      println(s)
    }
    var KeyArr2 = new Array[String](50)
    KeyArr2 = distinctSet.take(30)
   distinctSet.saveAsTextFile("distinct");
    println(distinctSet.count())
    println("Distinct")
    for(s <- KeyArr2){
      println(s)
    }
  }

}
