package org.mongo.twitter_graph.Preprocessing.Cleaning;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author Lassoued Anouer
 */
public class WordCleaner extends BaseFilter {
   public WordCleaner(){}
   
   public String removeSpecialChars(String word){
       String flag = "";
       int head=0,tail=word.length()-1,start=-1,end=-1;
       do{
           if(start==-1){
                if(isLetter(word.charAt(head))){
                    start = head;
                }else{
                    head++;
                }
           }
           if(end==-1){
               if(isLetter(word.charAt(tail))){
                    end = tail;
                }
               else{
                   tail--;
               }
           }
       }while(start==-1||end==-1);
       return word.substring(start, end+1);
   }

   public int startLetter(String word){
       int flag = 0;
       int head=0,start=-1;
       do{
           if(start==-1){
                if(isLetter(word.charAt(head))){
                    start = head;
                    flag = start;
                }else{
                    head++;
                }
           }           
       }while(head<word.length()&&start==-1);
       return flag;
   }
   
   public int endLetter(String word){
       int flag = word.length()-1;
       int head=0,tail=word.length()-1,start=-1,end=-1;
       do{           
           if(end==-1){
               if(isLetter(word.charAt(tail))){
                    end = tail;
                    flag = end;
                }
               else{
                   tail--;
               }
           }
       }while(end==-1&&tail>=0);
       return flag +1;
   }

   public boolean isLetter(char letter){
       boolean flag = false;
       if((letter>='A'&&letter<='Z')||(letter>='a'&&letter<'z')||(letter>='0'&&letter<='9')){
           flag = true;
       }
       return flag;
   }

    @Override
    public boolean isKeep(TridentTuple tridentTuple) {
        return false;
    }
}
