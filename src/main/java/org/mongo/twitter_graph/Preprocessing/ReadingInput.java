/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mongo.twitter_graph.Preprocessing;

//import org.json.JSONObject;


/**
 *
 * @author RestyLouis
 */
public class ReadingInput {
    
    public ReadingInput(){}
    
    /*public ArrayList<User> getInputs(){
        ArrayList<User> flag = new ArrayList();
        TweetCleaner clean = new TweetCleaner();
        try{
            FileReader fr = new FileReader("DataSet\\Starbucks.txt");
            BufferedReader read = new BufferedReader(fr);
            String line;
            JSONObject jsonLine;
            TweetBean tb = new TweetBean();
            UserBean ub = new UserBean();
            while((line = read.readLine())!=null){                                
                jsonLine = new JSONObject(line);
                //System.out.println(jsonLine.toString());
                tb = new TweetBean();
                ub = new UserBean();
                tb.setTweetID(Long.toString(jsonLine.getLong("tweetId")));
                
                tb.setTweetTXT(jsonLine.getString("tweetMsg"));
                //tb.setTweetTXT(clean.cleanTweetText(tb.getTweetTXT()));
                tb.setDate(jsonLine.getString("tweetDate"));
                tb.setLanguage(jsonLine.getString("tweetLang"));
                tb.setLocation(jsonLine.getString("tweetLoc"));
                tb.setUserID(Long.toString(jsonLine.getLong("userId")));
                //tb.setTopicID(jsonLine.getInt("topicId"));
                ub.setUserid(tb.getUserID());
                ub.setScreenName(clean.removeNonASCII(jsonLine.getString("userScreenName")));
                ub.setFullName(clean.removeNonASCII(jsonLine.getString("userName")));
                ub.setAddress(clean.removeNonASCII(jsonLine.getString("userLoc")));
                if(jsonLine.has("userGender"))
                    ub.setGender(jsonLine.getString("userGender").charAt(0));
                else
                    ub.setGender('u');
                
                if(tb.getLanguage().compareToIgnoreCase("en")==0||tb.getLanguage().compareToIgnoreCase("tl")==0)
                    flag.add(new TweetUser(tb,ub));
            }            
            
                      
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return flag;
    }*/
}
