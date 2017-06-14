import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by USER on 13-06-2017.
 */
public class NLPdemo {

    public void process(String line){

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse,");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        if (line != null && line.length() > 0) {


            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                for (CoreLabel token:tokens){
                    String word = token.toString();
                    String lem = token.get(CoreAnnotations.LemmaAnnotation.class);
                    String pos= token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    String namedEntity = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    System.out.println("word : "+word+",Lemmatization: "+lem+" , Pos : "+pos+" , Named Entity : "+namedEntity);

                }


                //Corference Resolution of the text

                //Map<Integer, CorefChain> graph = annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);
                //System.out.println(graph);

            }
        }
    }

    public static void main(String[] args) {

        NLPdemo nlp = new NLPdemo();
        nlp.process("Add some useful information about your project from analytics graph to Wiki Page.");
    }
}
