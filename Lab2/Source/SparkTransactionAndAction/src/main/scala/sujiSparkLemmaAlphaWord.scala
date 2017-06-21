import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by putha on 6/20/2017.
  */
object sujiSparkLemmaAlphaWord {
  def main(args: Array[String]) {

    System.setProperty("sujihome.sujihadoop.sujidir","C:\\Users\\putha\\Desktop\\KDM\\winutils");

    val sujiSparkConf = new SparkConf().setAppName("SujiSparkWordCount").setMaster("local[*]")

    val sujisparkcontext=new SparkContext(sujiSparkConf)

    val SujiinputTextFile=sujisparkcontext.textFile("src//main//InPutFile//braclLemmatized")
    //val SujiinputTextFile=sujisparkcontext.textFile("input");

    val Sujiwc=SujiinputTextFile.flatMap(sujiline=>{sujiline.split(" ")}).map(Sujiword=>(Sujiword.toUpperCase().charAt(0),1)).cache()

    //val Sujioutput=Sujiword.reduceByKey(_+_)

    Sujiwc.saveAsTextFile("src//main//OuTputFile//assioutput")

    val sujiOut=Sujiwc.collect()

    val GroubByFirst=SujiinputTextFile.groupBy(_.substring(0,1))

    var sujiString:String="Lemmatized Word Count based on alphabets for the input file are being displayed:Counting the word\n"

    GroubByFirst.foreach
    {
      case(sujiword,sujicount)=>{

        sujiString+=sujiword+" : "+sujicount+"\n"

    }}

    val sujiwordRDD = SujiinputTextFile.flatMap(_.split(" "))

    val sujiFlatMap = sujiwordRDD.groupBy(_.charAt(0))

    sujiFlatMap.saveAsTextFile("src//main//OuTputFile//BrackOutput")

    val sujiMApCollect=sujiFlatMap.collect()


  }

}
