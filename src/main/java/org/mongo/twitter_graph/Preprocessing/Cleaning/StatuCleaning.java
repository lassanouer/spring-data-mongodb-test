package org.mongo.twitter_graph.Preprocessing.Cleaning;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Lassoued on 12/04/2015.
 */
public class StatuCleaning {

    public String clean(String status) {
        status = status.replace("...", " ");
        status = status.replace(",", " ");
        status = status.replace("..", " ");
//            System.out.println(status);
        status = status.replaceAll("http[^ ]*", " ");
//        status = status.replaceAll("\".*\"", " ");
        status = status.replaceAll("http.*[\r|\n]*", " ");
        status = status.replaceAll(" +", " ");

        return status;
    }

    public String removePunctuationSigns(String string) {
        string = StringUtils.removeEnd(string, "'s");
        string = StringUtils.removeEnd(string, "’s");
        String punctuation = "!?.'’\":-+,$&()#=*";
        char[] chars = punctuation.toCharArray();
        for (char currChar : chars) {
            string = StringUtils.replace(string, String.valueOf(currChar), " ");
        }
        return string.trim();
    }
}
