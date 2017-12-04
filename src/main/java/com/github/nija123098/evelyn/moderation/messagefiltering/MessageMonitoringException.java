package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.exception.BotException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringException extends BotException {
    public MessageMonitoringException(String message) {
        super(message);
    }
}
