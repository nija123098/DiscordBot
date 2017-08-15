package com.github.kaaz.emily.util;

import org.apache.commons.validator.UrlValidator;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class NetworkHelper {
    private static final UrlValidator VALIDATOR = new UrlValidator(new String[]{"http", "https"});
    public static String stripProtocol(String s){
        if (s.startsWith("http")) {
            s = s.substring(4);
            if (s.startsWith("s")) s = s.substring(1);
            s = s.substring(3);
        }
        return s;
    }
    public static boolean isValid(String url){
        return VALIDATOR.isValid((url.startsWith("http") ? "" : "https://") + url);
    }
}
