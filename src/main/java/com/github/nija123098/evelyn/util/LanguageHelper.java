package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exeption.ArgumentException;

import java.util.*;

/**
 * Made by nija123098 on 4/15/2017.
 */
public class LanguageHelper {
    private static final Set<String> TRUE = new HashSet<>(), FALSE = new HashSet<>();
    static {
        Collections.addAll(TRUE, "true", "t", "yes", "y", "ya", "sure", "1", "one", "defiantly", "absolutely", "ya", "ye", "enable", "affirmative");
        Collections.addAll(FALSE, "false", "f", "no", "n", "naw", "nope", "0", "zero", "nein", "disable", "negative");
    }
    public static boolean getBoolean(String s){
        s = s.toLowerCase();
        if (TRUE.contains(s)) return true;
        else if (FALSE.contains(s)) return false;
        throw new ArgumentException("That doesn't appear to be an affirmation or negation, try true or false.  Received: " + s);
    }
    private static final List<String> INTEGERS;
    static {
        INTEGERS = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen");
    }
    public static int getInteger(String s){
        s = s.replace(",", "").replace(".", "").replace("_", "");
        try{return Integer.parseInt(s);
        }catch(NumberFormatException ignored){}
        s = s.toLowerCase().replace(" and ", "");
        int wordVal = INTEGERS.indexOf(s);
        if (wordVal != -1) return wordVal;
        if (s.endsWith("teen")) {
            wordVal = INTEGERS.indexOf(s.substring(0, s.length() - 4));
            if (wordVal != -1) return wordVal + 10;
        }
        throw new ArgumentException("Either that is not a number or you spelled it out and it's value is over 12");
    }
    public static String makePleural(String s){
        return s + (s.endsWith("s") ? "'s" : "s");
    }
    public static String makePossessive(String s){
        return s + "'" + (s.endsWith("s") ? "" : "s");
    }
    public static String getInteger(int val){
        return INTEGERS.size() > val ? INTEGERS.get(val) : "number";
    }
}
