package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.awt.*;

/**
 * A exception which can be made into a message.
 * An exception subclassing this class does not
 * necessarily indicate that something went wrong.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class BotException extends RuntimeException {
    public BotException() {
    }

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotException(Throwable cause) {
        super(cause);
    }

    /**
     * Generates a {@link MessageMaker} for the instance exception.
     *
     * @param channel the channel to set as the destination.
     * @return the {@link MessageMaker} instance which can displays the exception.
     */
    public MessageMaker makeMessage(Channel channel){
        return new MessageMaker(channel).maySend().withColor(Color.RED).getHeader().appendRaw(this.getMessage()).getMaker().getTitle().appendRaw(this.getClass().getSimpleName()).getMaker().withTimestamp(System.currentTimeMillis());
    }
}
