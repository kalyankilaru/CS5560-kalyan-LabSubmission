import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by putha on 6/20/2017.
  */
object sparkActionAndTransaction {
  def main(args: Array[String]) {

    System.setProperty("hadoopLocal.homeroot.dirurl", "C:\\Users\\putha\\Desktop\\KDM\\winutils");

    val sparkConfSuji = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val scsuji=new SparkContext(sparkConfSuji)


    val inputsuji=scsuji.textFile("InputFileSuji/tohoku.earthquake.tsunami.8.txt")


    val Sujiinput1=scsuji.textFile("InputFileSuji/tohoku.earthquake.tsunami.9.txt");
    val Sujiinput2=scsuji.textFile("InputFileSuji/tohoku.earthquake.tsunami.12.txt");
    val SujiunionSet = Sujiinput1.union(Sujiinput2);
    val SujiintersectionSet = Sujiinput1.intersection(Sujiinput2);
    val SujidistinctSet = Sujiinput1.distinct(2);
    var KeyArrSuji = new Array[String](50)
    KeyArrSuji = SujiunionSet.take(30)
    SujiunionSet.saveAsTextFile("OutputFileSuji/unionOFWords");
    println(SujiunionSet.count())

    //Displaying all the contents from both files
    println("Collection of all words")
    for(stringUnionSuji <- KeyArrSuji){
      println(stringUnionSuji)
    }

    var SujiKeyArr1 = new Array[String](50)
    SujiKeyArr1 = SujiintersectionSet.take(30)
    SujiintersectionSet.saveAsTextFile("OutputFileSuji/intersectionWords");
    println(SujiintersectionSet.count())
    println("Intersection of words between the file")
    for(stringInter <- SujiKeyArr1){
      println(stringInter)
    }

    var SujiKeyArr2 = new Array[String](50)
    SujiKeyArr2 = SujidistinctSet.take(30)
    SujidistinctSet.saveAsTextFile("OutputFileSuji/distinctWords");
    println(SujidistinctSet.count())
    println("Distinct of words between the files")
    for(stringDistinct <- SujiKeyArr2){
      println(stringDistinct)
    }
  }

}
