package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.awt.*;

/**
 * Written by Soarnir 21/11/17
 */

public class InsufficientException extends BotException {

    public InsufficientException(String message) {
        super(message);
    }

    public InsufficientException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientException(Throwable cause) {
        super(cause);
    }

    @Override
    public MessageMaker makeMessage(Channel channel) {
        return super.makeMessage(channel).getNote().append("Please check the help command for more information on how to use this command.").getMaker().withColor(new Color(255, 183, 76)).getTitle().clear().appendRaw("Insufficient Amount").getMaker();
    }
}
