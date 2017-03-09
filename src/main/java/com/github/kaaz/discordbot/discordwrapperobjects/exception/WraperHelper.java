package com.github.kaaz.discordbot.discordwrapperobjects.exception;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * Made by nija123098 on 3/8/2017.
 */
public class WraperHelper {
    static void wrapException(RuntimeException e) {
        if (e instanceof DiscordException){
            throw new DException((DiscordException) e);
        }
        throw e;
    }
    static void wrapExeptionWithPerms(RuntimeException e) {
        if (e instanceof MissingPermissionsException){
            throw new MissingPermException((MissingPermissionsException) e);
        }
        wrapException(e);
    }
}
