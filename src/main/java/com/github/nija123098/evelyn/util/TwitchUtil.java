package com.github.nija123098.evelyn.util;

import static com.github.nija123098.evelyn.util.NetworkHelper.isValid;
import static com.github.nija123098.evelyn.util.NetworkHelper.stripProtocol;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TwitchUtil {
    public static String extractCode(String s) {
        s = stripProtocol(s);
        if (s.startsWith("www.twitch.tv/")) s = s.substring(14);
        else return null;
        return isValid("https://www.twitch.tv/" + s) ? s : null;
    }
}
