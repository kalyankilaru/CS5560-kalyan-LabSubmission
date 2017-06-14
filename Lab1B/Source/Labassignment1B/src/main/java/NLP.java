import com.sun.corba.se.impl.orbutil.graph.GraphImpl;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
/**
 * Created by USER on 13-06-2017.
 */
public class NLP {

    private DataReader  file = new DataReader();
    private Map<String,Set<String>> nameEntityMap = new HashMap();

    Properties propts = new Properties();
    StanfordCoreNLP pipeline;


    public void process(){


        propts.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse,");
        pipeline = new StanfordCoreNLP(propts);

        String filePath = "C:\\Users\\USER\\Desktop\\cat-descriptions_120396.txt";
        System.out.println("reading the file from the given file path");

        //Calling the method from the DataReader class for reading the file
        String sentence = file.readData(filePath);
        parse(sentence);

        System.out.println("Ask any question to the system\n Enter start or exit");

        Scanner scan = new Scanner(System.in);

        while(scan.hasNext())
        {

            String question = scan.nextLine();

            if(!question.equalsIgnoreCase("exit")){

                String answer = extract(question);
                if (answer != null)
                {
                 System.out.println("The Answer is:"+answer);
                }
                else
                {
                    System.out.println("Answer cannot be extracted");
                }
            }

            else
            {
                break;
            }
        }
    }

    public void parse(String document)
    {
        if (document != null && document.length()>0){
            int longest = 0;

            Annotation annotation = pipeline.process(document);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)){

                //Making Tokens
                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

                for(CoreLabel token:tokens){
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    if(!ne.equalsIgnoreCase("O")){
                        Set<String> nerList = nameEntityMap.get(ne);
                        String[] t = token.toString().split("-");
                        if(nerList == null){
                            nerList = new HashSet<>();
                        }
                        nerList.add(t[0]);
                        nameEntityMap   .put(ne,nerList);
                    }
                }
                // Tree of the sentence in the document
                Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                //System.out.println("parse tree:\n" + tree);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
                //System.out.println("dependency graph:\n" + dependencies);
                // this is the Stanford dependency graph of the current sentence
                /*SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
                System.out.println("dependency graph:\n" + dependencies);*/


                /*Map<Integer, CorefChain> graph =
                        annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);

                System.out.println(graph);*/
            }
        }
    }
    public String extract(String question){
        String answer = null;

        parseQuestion(question);

        return answer;
    }

    public String parseQuestion(String question) {
        String ans = null;
        if (question != null && question.length() > 0) {
            Annotation annotation = pipeline.process(question);
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for(CoreMap sentence : sentences){
                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                String questions[] = question.split(" ");
                String questionType = questions[0];
                String verb = questions[1];
                String obj = questions[2];
                String ne = tokens.get(2).get(CoreAnnotations.NamedEntityTagAnnotation.class);
                switch (questionType){
                    case "who"   : ans = whoType(verb,obj,ne);
                        break;
                    case "where" : ans = whereType(verb,obj,ne);
                        break;
                    case "when"  : ans = whenType(verb,obj,ne);
                        break;
                }
            }
        }
        return ans;
    }

    public String whoType(String verb, String obj,String ne){
        System.out.println("Who type question : "+ne+", is object entity" );
        return null;
    }

    public String whereType(String verb, String obj,String ne){
        System.out.println("Where type question : "+ne+", is object entity" );
        return null;
    }
    public String whenType(String verb, String obj,String ne){
        System.out.println("When type question : "+ne+", is object entity" );
        return null;
    }


        public static void main(String[] args){
            NLP nlp = new NLP();
            nlp.process();
        }

}
