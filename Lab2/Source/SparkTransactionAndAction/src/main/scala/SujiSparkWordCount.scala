import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by putha on 6/14/2017.
  */
object SujiSparkWordCount {
  def main(args: Array[String]) {

    System.setProperty("home.sujihadoop.dir","C:\\Users\\putha\\Desktop\\KDM\\winutils");

    val sujiSparkConf = new SparkConf().setAppName("SujiSparkWordCount").setMaster("local[*]")

    val sujisparkcontext=new SparkContext(sujiSparkConf)

    val SujiinputTextFile=sujisparkcontext.textFile("src//main//InPutFile//assi")
    //val SujiinputTextFile=sujisparkcontext.textFile("input");

    val Sujiwc=SujiinputTextFile.flatMap(sujiline=>{sujiline.split(" ")}).map(Sujiword=>(Sujiword,1)).cache()

    val Sujioutput=Sujiwc.reduceByKey(_+_)

    Sujioutput.saveAsTextFile("src//main//OuTputFile//assioutput")

    val sujiOut=Sujioutput.collect()

    var sujiString:String="Word Count for the input file are being displayed:Counting the word\n"
    sujiOut.foreach{case(sujiword,sujicount)=>{

      sujiString+=sujiword+" : "+sujicount+"\n"

    }}

  }

}

