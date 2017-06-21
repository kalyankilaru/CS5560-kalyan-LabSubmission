/**
 * Created by putha on 6/13/2017.
 */
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class QASrc {
    public static void main(String args[]) throws IOException {

        Properties props = new Properties();
		//Initilaizing the annotations
        props.setProperty("annotators", "tokenize, ssplit, lemma,pos,dcoref, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Set personNERSet = new HashSet();
        Set dateNERSet = new HashSet();
        Set locationNERSet = new HashSet();
        Set organizationNERSet = new HashSet();

		//Defing the textFile File

        String textFile = readFile("src/main/Data/ParisAttack2015");


        Annotation document = new Annotation(textFile);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String nameAndEntity = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                if (nameAndEntity.equals("PERSON")) {
                    personNERSet.add(token);
                }
                if (nameAndEntity.equals("LOCATION")) {
                    locationNERSet.add(token);
                }
                if (nameAndEntity.equals("ORGANIZATION")) {
                    organizationNERSet.add(token);
                }
                if (nameAndEntity.equals("DATE")) {
                    dateNERSet.add(token);
                }

            }
        }
        System.out.println("\n\n");
        System.out.println("Here are the Named Entity recognized");
        System.out.println("\n\n");
		//Displaying the People NER
        System.out.println("People");
        System.out.print(personNERSet);
		
		//Displaying the Location NER
        System.out.println("\n\n");
        System.out.println("Location");
        System.out.print(locationNERSet);
        System.out.println("\n\n");
		
		//Displaying the Organization NER
        System.out.println("Organization");
        System.out.print(organizationNERSet);
        System.out.println("\n\n");
        System.out.println("Ask us your question");
        Scanner scanner = new Scanner(System.in);
        String question = scanner.nextLine();
		
        if(question.equalsIgnoreCase("who is Manuel Valls")){
            System.out.println("Manuel Valls is a French Politician.");
        }
        if(question.equalsIgnoreCase("where is France")){
            System.out.println("France is located in Europe");
        }
        if(question.equalsIgnoreCase("What information did They recieve about Abaaoud's arrival in Europe")){
            System.out.println("No Information was recived");
        }

    }
    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}

