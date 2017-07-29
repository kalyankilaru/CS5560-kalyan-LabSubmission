/**
 * Created by VenkatNag on 7/18/2017.
 */
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Quadruple;
import rita.RiWordNet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Quadruple;
import rita.RiWordNet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class call {
    public static String returnTriplets(String sentence) throws IOException {

        Document doc = new Document(sentence);
        String triplet = "";
        //FileWriter subjectFile = new FileWriter("subjectFiles");
        FileWriter fileWriter = new FileWriter("ObjectProperties",true);
        FileWriter fileSubject = new FileWriter("Class",true);
        FileWriter fileTriplets = new FileWriter("Triplets",true);
        FileWriter fileData = new FileWriter("DataProperties",true);
        FileWriter fileIndividual = new FileWriter("Individuals",true);

        ArrayList<String> subjectList = new ArrayList<>();
        ArrayList<String> objectList = new ArrayList<>();
        ArrayList<String> predicateList = new ArrayList<>();
        HashSet<String> predicateValues = new HashSet();
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            Collection<Quadruple<String, String, String, Double>> l = sent.openie();//.iterator();

            // Quadruple x = l.next();
            for (Quadruple x : l) {

                //  while (l.hasNext()){
                //    Quadruple x = l.next();
                String subject = (String) x.first();
                subjectList.add(subject);

                String object = (String) x.third();
                objectList.add(object);
                //subjectFile.write(subject);
                String predicate = (String) x.second();
                if(!predicateValues.contains(predicate))
                    predicateValues.add(predicate);

                String predicateValue = "predicates";
                FileWriter predicateFile = new FileWriter(predicateValue,true);

                for(String str : predicateValues){
                    predicateFile.write(str+"\n");
                }
                predicateFile.close();




                for(String str : predicateValues){
                    if(!predicateList.contains(predicate)) {
                        if (str.contains("emphasiz")) {
                            if (!predicateList.contains("emphasiz"))
                                fileWriter.write("emphasize" + "," + subject + "," + object + ",Func\n");
                        } else if (str.contains("feature")) {
                            if (!predicateList.contains("feature"))
                                fileWriter.write("features" + "," + subject + "," + object + ",Func\n");
                        } else if (str.contains("determine")) {
                            if (!predicateList.contains("determine"))
                                fileWriter.write("determines" + "," + subject + "," + object + ",Func\n");
                        } else if (str.contains("defeat")) {
                            if (!predicateList.contains("defeat"))
                                fileWriter.write("defeated" + "," + subject + "," + object + ",Func\n");
                        } else if (str.contains("play")) {
                            if (!predicateList.contains("play"))
                                fileWriter.write("played" + "," + subject + "," + object + ",Func\n");
                        } else if (str.contains("was")) {
                            if (!predicateList.contains("was"))
                                fileWriter.write("was" + "," + subject + "," + object + ",Func\n");
                        }
                        else if (str.contains("finish")) {
                            if (!predicateList.contains("finish"))
                                fileWriter.write("finished" + "," + subject + "," + object + ",Func\n");
                        }
                        else if (str.contains("in")) {
                            if (!predicateList.contains("is in"))
                                fileWriter.write("is in" + "," + subject + "," + object + ",Func\n");
                        }
                        else if (str.contains("with")) {
                            if (!predicateList.contains("is with"))
                                fileWriter.write("is with" + "," + subject + "," + object + ",Func\n");
                        }
                        else if (str.contains("join")) {
                            if (!predicateList.contains("join"))
                                fileWriter.write("join" + "," + subject + "," + object + ",Func\n");
                        }
                        else if (str.contains("found")) {
                            if (!predicateList.contains("found"))
                                fileWriter.write("found" + "," + subject + "," + object + ",Func\n");
                        }
                        predicateList.add(predicate);
                    }
                    else
                    {
                        predicateList.add(predicate);
                    }


                }

                //creating Triplets file
                String ner = returnNER(object);
                if(ner.equals("O")){
                    fileTriplets.write(subject+","+predicate+","+object+",Data\n");
                }
                else{
                    fileTriplets.write(subject+","+predicate+","+object+",Obj\n");
                }



                triplet = subject + predicate + object;

            }



            BufferedReader br = new BufferedReader(new FileReader("data/stopwords.txt"));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                for (int i = 0; i < subjectList.size(); i++) {
                    if(subjectList.get(i).equals(line)){
                        subjectList.remove(i);
                    }
                }
            }

            //removing stopwords and duplicates for the subject
            HashSet<String> subjectSet = stopWordRemoving(subjectList);

            //adding all subjects to the class file
            for(String str : subjectSet){
                fileSubject.write(str+"\n");
            }

            //creating triplets file



            //appending the hasNER relation
            String subjectNER = "";
            for(String str : subjectSet){
                subjectNER = returnNER(str);
                fileWriter.write("hasNer" + "," + str + "," + subjectNER + ",Func\n");
            }

            //appending the synonym relation
            HashSet<String> synonymSet = new HashSet<>();
            for(String str : subjectSet){
                synonymSet = getSynonyms(str);
                for(String syn : synonymSet) {
                    fileWriter.write("relatedTo" + "," + str + "," + syn + ",Func\n");
                }
            }

            //creating the Individuals file
            for(String str : subjectSet){
                fileIndividual.write(str+","+returnNER(str)+"\n");
            }


            //creating data properties
            for(String str : subjectSet){
                fileData.write("hasNer,"+str+",string\n");
            }



        }
        fileData.close();
        fileIndividual.close();
        fileSubject.close();
        fileTriplets.close();
        fileWriter.close();
        return triplet;
    }

    public static String returnNER(String word){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(word);
        pipeline.annotate(document);
        String stringNER = "";
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                stringNER = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            }
        }
        return stringNER;
    }

    public static HashSet<String> stopWordRemoving(ArrayList arrayList) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader("data/stopwords.txt"));

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            for (int i = 0; i < arrayList.size(); i++) {
                if(arrayList.get(i).equals(line)){
                    arrayList.remove(i);
                }
            }
        }
        HashSet<String> subjectSet = new HashSet<String>(arrayList);
        return subjectSet;
    }

    public static HashSet<String> getSynonyms(String word){
        RiWordNet wordnet = new RiWordNet("E:\\UMKC\\Sum_May\\KDM\\WordNet-3.0");
        String[] poss = wordnet.getPos(word);
        HashSet<String> synonym = new HashSet<>();
        for (int j = 0; j < poss.length; j++) {
            System.out.println("\n\nSynonyms for " + word + " (pos: " + poss[j] + ")");
            String[] synonyms = wordnet.getAllSynonyms(word,poss[j],10);
            for (int i = 0; i < synonyms.length; i++) {
                synonym.add(synonyms[i]);
            }
        }
        return synonym;
    }

}
