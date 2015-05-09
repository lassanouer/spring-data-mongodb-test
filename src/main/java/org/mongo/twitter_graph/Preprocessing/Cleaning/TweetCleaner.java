package org.mongo.twitter_graph.Preprocessing.Cleaning;

/*import org.mongo.twitter_graph.domain.Post;
import org.tartarus.snowball.ext.PorterStemmer;
import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Lassoued Anouer
 */

public class TweetCleaner// extends BaseFilter
 {
  /* private ArrayList<Abbreviation> abblist;
   private ArrayList<SmileyScores> smileys;
   
   public TweetCleaner(){
        abblist = getAbbs();
        smileys = getSmileys();
   }

    public String removeImageLinks(String tweetText){
        String flag=tweetText;
        StringTokenizer tokenize  = new StringTokenizer(flag);
        int size= tokenize.countTokens();
        String temp;
        flag="";
        for(int init=0;init<size;init++){
            temp = tokenize.nextToken();
            if(!(temp.contains("http://t.co/"))&&!(temp.charAt(0)=='@')&&!(temp.contains("https://t.co/"))){
                flag+= temp + " ";
            }
        }
        return flag;
    }

    
    
    private ArrayList<Abbreviation> getAbbs(){
        ArrayList<Abbreviation> flag = new ArrayList();
        try{
            FileReader fr = new FileReader("DataSource\\Acronyms.txt");
            BufferedReader read = new BufferedReader(fr);
            
            String word=null;
            do{
                word = read.readLine();                
                if(word!=null){
                    StringTokenizer token = new StringTokenizer(word,"	");
                    flag.add(new Abbreviation(token.nextToken(), token.nextToken()));
                }
            }while(word!=null);
            
        }catch(Exception e){
        
        }
        
        return flag;
    }
    public String fixAbb(String tweet){
        String flag = "";
         WordCleaner wordClean = new WordCleaner();
        StringTokenizer token = new StringTokenizer(tweet," ");
        int size = token.countTokens(),index,start,end;
        String tempWord,word;
        for(int counter = 0;counter<size;counter++){
            word = token.nextToken();
            start = wordClean.startLetter(word);
            end = wordClean.endLetter(word);
            tempWord = word.substring(start,end);
            index =isAbbreviation(tempWord);
            if(index>=0){                
               flag+= word.toLowerCase().replace(abblist.get(index).getAbbreviation().toLowerCase(), abblist.get(index).getOriginalForm())+ " ";
            }else{
                flag += word + " ";
            }
        }
        return flag;
    }
    
    
    private int isAbbreviation(String word){
        int flag = -1;
           for(int init=0;init<abblist.size();init++){               
                if(word.toUpperCase().compareToIgnoreCase(abblist.get(init).getAbbreviation())==0){
                    flag = init; 
                    System.out.println(flag);
                    break;
                }
            }
        return flag;
    }
    
   
    
    private boolean isSmiley(String word){
         boolean flag = false;
         for(int init=0;init<smileys.size();init++){
            if(word.compareToIgnoreCase(smileys.get(init).getSmiley())==0){
                flag = true;
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
     }
     
     
    
    public Post separate(String tweetText){
        Post flag = new Post();
        StringTokenizer tokens = new StringTokenizer(tweetText);
        int size=tokens.countTokens();
        String temp;
        for(int init=0;init<size;init++){
            temp = tokens.nextToken();
            if(isSmiley(temp)){
                //flag.setSmileys(flag.getSmileys() + " " + temp);
            }else if (temp.charAt(0)=='#'){
                //flag.setHashtag(flag.getHashtag() + " " + temp);
            }else{
                flag.setText(flag.getText() + " " + temp);
            }        
        }
        
        return flag;
    }

    //***************************************Should start 3rd*******************************************
    public String cleanTweetText(String post){
        String temp = post.toLowerCase();
        return ((temp.replaceAll("RT", "")).replaceAll("^http", "")).replaceAll("[^\\w\\s]","");
    }

    //***************************************Should start first*******************************************
    //   SMILEY:    key = ;)   value = mot
    public String replaceSmileys(String post, HashMap smile,String lang) throws IOException {
        chargerSmiley(lang);
        StringTokenizer st = new StringTokenizer(post, " ");
        String postMot;
        String temp = null;
        while(st.hasMoreTokens()){
            postMot = st.nextToken();
            if (smile.containsKey(postMot)){
                postMot = (String) smile.get(postMot);
            }
            temp.concat(" "+postMot);
        }
        return temp;
    }

    public void chargerSmiley(String lang){
        //**********************
        //       Upload file from mongo
        //       et la charger dans un Hash map
    }

    //***************************************Should start second*******************************************
    public String removeStopWords(String post){
        return null;
    }

    //***************************************Should start 4th*******************************************
    public String stemming(String post){
        StringTokenizer tokenizer = new StringTokenizer(post," ");
        String temp = "";
        do{
            PorterStemmer stemmer = new PorterStemmer();
            stemmer.setCurrent(tokenizer.nextToken());
            if (stemmer.stem()) {
                temp.concat(stemmer.getCurrent()+" ");
            }
        }while (tokenizer.hasMoreTokens());
        return null;
    }


    @Override
    public boolean isKeep(TridentTuple tridentTuple) {
        //enchainer les filters commun pour tt les langues
        return true;
    }*/
}
