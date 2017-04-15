package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.ArgumentException;

import java.util.*;

/**
 * Made by nija123098 on 4/15/2017.
 */
public class LanguageHelper {
    private static final Set<String> TRUE = new HashSet<>(), FALSE = new HashSet<>();
    static {
        Collections.addAll(TRUE, "yes", "ya", "sure", "1", "one", "defiantly", "absolutely", "ya", "ye");
        Collections.addAll(FALSE, "no", "naw", "nope", "no thanks", "0", "zero", "nein");
    }
    public static boolean getBoolean(String s){
        s = s.toLowerCase();
        if (TRUE.contains(s)){
            return true;
        }else if (FALSE.contains(s)){
            return false;
        }
        throw new ArgumentException("That doesn't appear to be an affirmation or negation, try true or false");
    }
    private static final List<String> INTEGERS;
    static {
        INTEGERS = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve");
    }
    public static int getInteger(String s){
        s = s.replace(",", "").replace(".", "").replace("_", "");
        try{return Integer.parseInt(s);
        }catch(NumberFormatException ignored){}
        s = s.toLowerCase().replace(" and ", "");
        int wordVal = INTEGERS.indexOf(s);
        if (wordVal != -1){
            return wordVal;
        }
        throw new ArgumentException("Either that is not a number, of you spelled it out and it's value is over 12");
    }
}
