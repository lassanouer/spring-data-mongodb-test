package org.mongo.twitter_graph.Preprocessing.Cleaning;

import org.mongo.twitter_graph.domain.Hashtag;
import java.util.ArrayList;

/**
 * @author Lassoued Anouer
 *
 */
public class MostCommon {
    
    
    public MostCommon(){
    }
    
    /*public ArrayList<Hashtag> getMostUsedHashtags(ArrayList<Post> ttList){
        int totalSize = ttList.size(),hashtags,tempIndex;
        ArrayList<Hashtag> flag = new ArrayList();
        StringTokenizer hashtagTokens;
        String tempHashtag;
        
         for(int init=0;init<totalSize;init++){
             hashtagTokens = new StringTokenizer(ttList.get(init).getHashtags().get(0));
             hashtags = hashtagTokens.countTokens();
             for(int hashtagCounters = 0;hashtagCounters<hashtags;hashtagCounters++){
                  tempHashtag = hashtagTokens.nextToken();
                  tempIndex = inList(flag,tempHashtag);
                  if(tempIndex>=0){
                      flag.get(tempIndex).setCount(flag.get(tempIndex).getCount() + 1);
                  }else{
                      flag.add(new Hashtag(tempHashtag,1));
                  }
             }
         }
         //return flag;
    }*/
    
    public int inList(ArrayList<Hashtag> hashtags,String hashtag){
        int flag = -1;
        
        for(int init=0;init<hashtags.size();init++){
            if(hashtags.get(init).getHashtag().compareToIgnoreCase(hashtag)==0){
                return init;
            }
        }
        return flag;
    }
}
