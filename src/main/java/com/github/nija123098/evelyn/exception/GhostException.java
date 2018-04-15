package com.github.nija123098.evelyn.exception;

/**
 * An exception thrown when a action which should not be done by
 * a bot running silently under the main bot is about to occur.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GhostException extends BotException {
    public GhostException() {
    }
    public static boolean isGhostCaused(Throwable throwable) {
        if (GhostException.class.isAssignableFrom(throwable.getClass())) return true;
        while ((throwable = throwable.getCause()) != null) {
            if (GhostException.class.isAssignableFrom(throwable.getClass())) return true;
        }
        return false;
    }
}
