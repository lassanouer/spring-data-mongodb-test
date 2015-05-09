package org.mongo.twitter_graph.Preprocessing;
/*
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.StanfordRedwoodConfiguration;
import SmileyScores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 *
 * @author Lassoued Anouer
 */

public class    SentimentAnalysis {
   /*   private StanfordRedwoodConfiguration pipeline;
      private ArrayList<SmileyScores> smileys;
     private TweetCleaner cleaner;
     
     public SentimentAnalysis(){
         Properties props = new Properties();
         props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

         pipeline = new StanfordRedwoodConfiguration(props);
         smileys = getSmileys();
         cleaner = new TweetCleaner();
     }
     
    
    public  float calculateSentiment (String text) {
        TweetText tt = cleaner.separate(text);
        
        float flag = calculateTextSentiment(tt.getText())*.5f;
        flag += hashtagScore(tt.getHashtag())*.25f;
        flag += smileyScore(tt.getSmileys())*.25f;
                
        if(flag>0){
            flag=1;
        }else if (flag<0){
            flag=-1;
        }
        return flag;
    }
    
    
    private float calculateTextSentiment(String text){
        float mainSentiment = 0;                                
        
        text = text.toLowerCase();
        int longest = 0;
        Annotation annotation = pipeline.process(text);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree) - 2;
            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment += sentiment;
                longest = partText.length();
            }
        }
        
        if(mainSentiment == 0 ){
            if(text.contains("hahaha")||text.contains("hehehe")){
                mainSentiment = 1;
            }else if (text.contains("huhuhu")){
                mainSentiment = -1;
            }
        }
                
       return mainSentiment;
    
    }
    
    
    private float hashtagScore(String text){
        float flag=0;
        StringTokenizer tokenize = new StringTokenizer(text);
        int size= tokenize.countTokens();
        String temp;
         
        for(int init=0;init<size;init++){
            temp = tokenize.nextToken();
           
            if(temp.charAt(0)=='#'){
                //System.out.println(cleaner.fixAbb(temp.substring(1,temp.length())));
                flag += calculateTextSentiment(cleaner.fixAbb(temp.substring(1,temp.length())));
            }
        }
        
         if(flag>1.0){
             flag = 1.0f;
         }else if (flag<-1.0){
             flag = -1.0f;
         }
         
        return flag;
    }
    
    
    
    private float smileyScore(String text){
        float flag=0;
        String  temp;
         float score;
         StringTokenizer token = new StringTokenizer(text);
         int size = token.countTokens();
         for(int init=0;init<size;init++){
             temp = token.nextToken();
             score = isSmiley(temp);
           
             if(score!=20){                 
                 flag += score;
             }             
         } 
         
         if(flag>1.0){
             flag = 1.0f;
         }else if (flag<-1.0){
             flag = -1.0f;
         }
        
        return flag;
    }
    
    
    private float isSmiley(String word){
         float flag = 20;
         for(int init=0;init<smileys.size();init++){
            if(word.compareToIgnoreCase(smileys.get(init).getSmiley())==0){
                flag = smileys.get(init).getScore();
               break;
            }
         }         
         return flag;
     }
    
     private ArrayList<SmileyScores> getSmileys(){
         ArrayList<SmileyScores> flag=  new ArrayList();
        try {
            FileReader fr = new FileReader("DataSource\\SmileyScores.txt");
            BufferedReader textReader = new BufferedReader(fr);
            String word=null;
            do{
                word = textReader.readLine();                
                if(word!=null){
                    StringTokenizer token = new StringTokenizer(word,",");
                    flag.add(new SmileyScores(token.nextToken(),Float.parseFloat(token.nextToken())));
                }
            }while(word!=null);                        
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }         
         return flag;
     }*/
}
