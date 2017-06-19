package com.github.kaaz.emily.util;

import org.apache.commons.validator.UrlValidator;

/**
 * Made by nija123098 on 6/11/2017.
 */
public class URLHelper {
    private static final UrlValidator VALIDATOR = new UrlValidator(new String[]{"http", "https"});
    public static boolean isValid(String url){
        return VALIDATOR.isValid(url);
    }
}
