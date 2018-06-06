package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.exception.BotException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringException extends BotException {
    private String blocked;
    private boolean deleteMessage;
    public MessageMonitoringException(String message, boolean deleteMessage) {
        super(message);
        this.deleteMessage = deleteMessage;
    }
    MessageMonitoringException(String message, String blocked) {
        this(message, true);
        this.blocked = blocked;
    }

    public String getBlocked() {
        return this.blocked;
    }

    public boolean deleteMessage() {
        return this.deleteMessage;
    }
}
