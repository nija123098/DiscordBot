package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.awt.*;

/**
 * Made by nija123098 on 3/31/2017.
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

    public MessageMaker makeMessage(Channel channel){
        return new MessageMaker(channel).maySend().withColor(Color.RED).getHeader().append(this.getMessage()).getMaker().getTitle().appendRaw(this.getClass().getSimpleName()).getMaker();
    }
}
