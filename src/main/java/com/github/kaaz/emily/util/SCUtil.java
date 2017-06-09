package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class SCUtil {
    public static String extractCode(String s){
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("soundcloud.com/")){
            return s.substring(15);
        }
        return null;
    }
}
