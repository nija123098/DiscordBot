package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class NetworkHelper {
    public static String stripProtocol(String s){
        if (s.startsWith("http")) {
            s = s.substring(4);
            if (s.startsWith("s")) s = s.substring(1);
            s = s.substring(3);
        }
        return s;
    }
}
