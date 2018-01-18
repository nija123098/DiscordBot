package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.exception.BotException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringException extends BotException {
    private String blocked;
    public MessageMonitoringException(String message) {
        this(message, message);
    }
    public MessageMonitoringException(String message, String blocked) {
        super(message);
        this.blocked = blocked;
    }

    public String getBlocked() {
        return this.blocked;
    }
}
