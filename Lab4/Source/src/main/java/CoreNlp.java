import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Quadruple;

import java.util.*;

/**
 * Created by chait on 04/07/2017.
 */
public class CoreNlp {


    private Map<String,Set<String>> nerMap = new HashMap();
    List<List<Quadruple<String, String, String, Double>>> l = new ArrayList<>();
    private  FileUtil f = new FileUtil();
    Properties props = new Properties();
    StanfordCoreNLP pipeline;
    int ansFlag = -1;
    int answer;

    public void initiateCoreNlp(){
        props.setProperty("annotators", "tokenize, ssplit,pos,lemma,ner, parse");
        pipeline = new StanfordCoreNLP(props);
    }

    public void startSystem(){
        System.out.println("Initiating coreNLP Lib...");
        initiateCoreNlp();
        String path = "C:\\Users\\chait\\Desktop\\004.txt";
        System.out.println("Reading input file at location:\n"+path);
        String line = f.readFromFile(path);//change file path here.
        System.out.println(line);
        parse(line);
        System.out.println("System stared....\n\nAsk questions,\nEnter 'quit' to exit");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String questionInPut = scanner.nextLine();
            if(!questionInPut.equalsIgnoreCase("quit") && questionInPut != null){
                extract(questionInPut);
            }
            else{
                break;
            }
        }
    }

    public void extract(String question){
        parseQuestion(question);
    }

    public void parseQuestion(String question) {
       Annotation annotation =  pipeline.process(question);
        String questionType = null;
        String sub = null;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            questionType = tokens.get(0).toString().toLowerCase().split("-")[0];
            sub = tokens.get(tokens.size()-1).toString().toLowerCase().split("-")[0];
            System.out.println("Question type : "+questionType);
        }
        switch(questionType){
            case "who" : printTriplets("PERSON",sub);
                        break;
            case "where" :
                    printTriplets("LOCATION",sub);
                    if(ansFlag == -1){
                        printTriplets("ORGANIZATION",sub);

                    }
                break;
            case "when" :
                    printTriplets("DATE",sub);
                    if(ansFlag == -1) {
                        printTriplets("DURATION", sub);
                    }

                break;
            case "howmuch" :
                    printTriplets("NUMBER",sub);
                if(ansFlag == -1) {
                    printTriplets("PERCENT", sub);
                }
                if(ansFlag == -1) {
                    printTriplets("MONEY", sub);
                }
                break;
            default: System.out.println("This question type is not supported yet");
        }
    }

    public void parse(String story){
        if (story != null && story.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(story);
            int id = 0;
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                //Tokenization
                id++;
                //System.out.println(id+" : "+sentence);
                returnTriplets(sentence.toString());
                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                for(CoreLabel token:tokens){
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                   // String v  = token.getString(CoreAnnotations.TextAnnotation.class);
                    //System.out.println("NE : "+ne);
                    if(!ne.equalsIgnoreCase("O")){
                        Set<String> nerList = nerMap.get(ne);
                        String[] t = token.toString().split("-");
                        if(nerList == null){
                            nerList = new HashSet<>();
                        }
                        nerList.add(t[0]);
                        nerMap.put(ne,nerList);
                    }
                }


            }
        }
    }

    //This piece of code fetches triplets using openie
    public void returnTriplets(String sentence){
        Document doc = new Document(sentence);
        for (Sentence sent : doc.sentences()) {
           l.add((List)sent.openie());
        }
    }
    // based on question type and keywords in question triplets are fetched.
    public void printTriplets(String keyToQueType,String sub){
            Set<String> nerList = nerMap.get(keyToQueType);
            boolean flag = true;
            if(nerList != null && nerList.size()!=0){
                for (String ele: nerList) {
                    if(flag){
                        Iterator it =  l.iterator();
                        while(it.hasNext() && flag){
                            List f = (List) it.next();
                            Iterator itr = f.listIterator();
                            while(itr.hasNext()){
                                String s = itr.next().toString().toLowerCase().replace(","," ").replace("("," ").
                                        replace(")"," ");
                                //System.out.println(s);
                                if(s.contains(ele.toLowerCase()) && s.contains(sub) && !ele.equalsIgnoreCase(sub)){
                                    System.out.println(s.substring(1,s.length()-5));
                                    flag = false;
                                    answer = 1;
                                    ansFlag = 1;
                                    break;
                                }
                            }
                        }
                    }
                    else
                        break;

                }
            }else{
                //System.out.println("No entity type found from data : "+keyToQueType+" - expected\nMore data is required!");
            }

    }

}
