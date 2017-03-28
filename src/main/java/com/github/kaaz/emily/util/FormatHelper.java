package com.github.kaaz.emily.util;

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
}
