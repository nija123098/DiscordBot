package com.github.nija123098.evelyn.exeption;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class GhostException extends BotException {
    public GhostException() {
    }
    public static boolean isGhostCaused(Throwable throwable){
        if (throwable.getClass().equals(GhostException.class)) return true;
        while ((throwable = throwable.getCause()) != null){
            if (throwable.getClass().equals(GhostException.class)) return true;
        }
        return false;
    }
}
