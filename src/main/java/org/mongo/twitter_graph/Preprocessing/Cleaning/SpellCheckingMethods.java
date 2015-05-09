package org.mongo.twitter_graph.Preprocessing.Cleaning;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lassoued on 12/04/2015.
 */
public class SpellCheckingMethods {

    public String repeatedCharacters(String currTerm) {
        String toReturn = currTerm;
        Integer index = null;
        Set<RepeatedLetters> setRL = new HashSet();
        int count = 1;
        char[] chars = currTerm.toCharArray();
        char currChar;
        char previousChar = 0;
        for (int i = 0; i < chars.length; i++) {
            currChar = chars[i];
            if (i > 0) {
                previousChar = chars[i - 1];
            }
            if (previousChar == currChar && StringUtils.isAlpha(String.valueOf(previousChar))) {
                if (index == null) {
                    index = i - 1;
                }
                count++;

            } else {
                if (count > 1) {
                    setRL.add(new RepeatedLetters(previousChar, index, count));
                    count = 1;
                }
                index = null;

            }
            if (i == (chars.length - 1) && count > 1) {
                setRL.add(new RepeatedLetters(previousChar, index, count));

            }
        }

        boolean loop = true;
        int loopsCounter = 0;
        while (loop) {
            loopsCounter++;
            if (loopsCounter > 5) {
                break;
            }
            for (RepeatedLetters rl : setRL) {
                String letter = String.valueOf(rl.getCurrChar());
                String toReplace;
                String subs;
                String toBeReplaced;

                //if two same letters are found
                if (rl.getCount() > 1) {
                    toBeReplaced = currTerm.substring(rl.getIndex(), rl.getIndex() + rl.getCount());

                    ///if these are actually 3 letters or more, test if by replacing them by 2 letters we have a match in the heuristics
                    if (rl.getCount() > 2) {
                        toReplace = letter + letter;
                        subs = StringUtils.replace(toReturn, toBeReplaced, toReplace);
                        /*if (ControllerBean.Hloader.getMapHeuristics().containsKey(subs.toLowerCase())) {
                            toReturn = subs;
                            loop = false;
                            break;
                        } else if (toReturn.endsWith(toReplace) && !toReturn.contains(" ")) {
                            toReturn = StringUtils.replace(toReturn, toBeReplaced, letter);
                            loop = true;
                            break;
                        }*/
                    }

                    // and maybe that if they are just one, this is a match too? (as in "boredd" meaning "bored")
//                    toReplace = letter;
//                    subs = StringUtils.replace(toReturn, toBeReplaced, toReplace);
//                    if (ControllerBean.Hloader.getMapHeuristics().containsKey(subs.toLowerCase())) {
//                        toReturn = subs;
//                        loop = false;
//                        break;
//                    }
                } else {
                    loop = false;
                }
            }
        }
        return toReturn;
    }

    private class RepeatedLetters {

        private char currChar;
        private int index;
        private int count;

        public RepeatedLetters(char currChar, int index, int count) {
            this.currChar = currChar;
            this.index = index;
            this.count = count;
        }

        public char getCurrChar() {
            return currChar;
        }

        public int getIndex() {
            return index;
        }

        public int getCount() {
            return count;
        }
    }
}
