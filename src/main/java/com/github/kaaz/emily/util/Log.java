package com.github.kaaz.emily.util;

import sx.blah.discord.Discord4J;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class Log {// todo upgrade
    public static void log(String message, Throwable...t){
        if (t.length > 0) Discord4J.LOGGER.error(message, t[0]);
        else Discord4J.LOGGER.info(message);
    }
}
