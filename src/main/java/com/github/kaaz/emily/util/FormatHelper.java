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
}
