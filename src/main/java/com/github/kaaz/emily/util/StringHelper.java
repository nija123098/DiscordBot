package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class StringHelper {
    public static String getFileType(String url){
        StringBuilder builder = new StringBuilder();
        for (int i = url.length() - 1; i > -1; --i) {
            if (url.charAt(i) == '.') break;
            else builder.append(url.charAt(i));
        }
        return builder.reverse().toString();
    }
    public static String ensureSize(String string, int size){
        if (string.length() <= size) return string;
        else return string.substring(0, size) + "...";
    }
}
