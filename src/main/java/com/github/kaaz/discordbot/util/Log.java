package com.github.kaaz.discordbot.util;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class Log {// todo upgrade
    public static void log(String message, Throwable...t){
        boolean error = t.length > 0;
        (error ? System.err : System.out).println(message);
        if (error){
            t[0].printStackTrace();
        }
    }
}
