package com.github.nija123098.evelyn.exeption;

/**
 * Should be thrown when a user has not properly set up a config.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class ConfigurationException extends BotException {
    public ConfigurationException(String message) {
        super(message);
    }
}
