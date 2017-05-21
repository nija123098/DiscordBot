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
        this.printStackTrace();
    }

    public DevelopmentException(String message) {
        super(message);
        this.printStackTrace();
    }

    public DevelopmentException(String message, Throwable cause) {
        super(message, cause);
        this.printStackTrace();
    }

    public DevelopmentException(Throwable cause) {
        super(cause);
        this.printStackTrace();
    }
}
