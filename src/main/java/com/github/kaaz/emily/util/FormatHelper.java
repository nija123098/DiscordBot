package com.github.kaaz.emily.util;

import java.util.Iterator;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class FormatHelper {
    public static String repeat(char c, int i) {
        String s = "";
        for (int j = 0; j < i; j++) {
            s += c;
        }
        return s;
    }
    public static String reduceRepeats(String s, char c){// use index of to optimize
        final StringBuilder builder = new StringBuilder();
        boolean repeat = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c){
                if (!repeat){
                    builder.append(c);
                }
                repeat = true;
            }else{
                repeat = false;
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }
    public static String removeChars(String s, char toRemove){
        StringBuilder builder = new StringBuilder(s.length());
        Iterator<Character> iterator = new StringIterator(s);
        iterator.forEachRemaining(character -> {
            if (character != toRemove) builder.append(character);
        });
        return builder.toString();
    }
    public static String trimFront(String s){
        if (!s.startsWith(" ")){
            return s;
        }
        boolean stop = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' '){
                stop = true;
            }else if (stop){
                return s.substring(i);
            }
        }
        return s;
    }
    public static String makePleural(String s){
        return s + "'" + (s.endsWith("s") ? s + "" : "s");
    }
}
