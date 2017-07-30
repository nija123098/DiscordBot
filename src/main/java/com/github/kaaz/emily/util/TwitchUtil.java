package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 6/11/2017.
 */
public class TwitchUtil {
    public static String extractCode(String s){
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("www.twitch.tv/")) s = s.substring(14);
        else return null;
        return NetworkHelper.isValid("https://www.twitch.tv/" + s) ? s : null;
    }
}
