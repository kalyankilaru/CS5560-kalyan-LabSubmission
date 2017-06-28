
/**
  * Created by Mayanka on 19-06-2017.
  */
object NGRAM {

  def main(args: Array[String]): Unit = {
    val a = getNGrams("The researchers will develop tools that use Bayesian statistical information to generate causal models from large and complex phenotyping datasets.",3)
    a.foreach(f=>println(f.mkString(" ")))
  }

  def getNGrams(sentence: String, n:Int): Array[Array[String]] = {
    val words = sentence
    val ngrams = words.split(' ').sliding(n)
    ngrams.toArray
  }

}


