package com.github.kaaz.emily.exeption;

/**
 * The exception thrown when there is an internal
 * error that is the developer's fault.
 *
 * @author nija123098
 * @since 2.0.0
 */
public class DevelopmentException extends BotException {
    public DevelopmentException() {
    }

    public DevelopmentException(String message) {
        super(message);
    }

    public DevelopmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DevelopmentException(Throwable cause) {
        super(cause);
    }
}
