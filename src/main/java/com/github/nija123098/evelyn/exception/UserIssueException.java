package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.awt.*;

/**
 * An {@link BotException} child class for super
 * classing exceptions which stem from the user's fault.
 */
public class UserIssueException extends BotException {
    private static final Color COLOR = new Color(255, 183, 76);
    public UserIssueException(String message) {
        super(message);
    }

    public UserIssueException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIssueException(Throwable cause) {
        super(cause);
    }

    @Override
    public MessageMaker makeMessage(Channel channel) {
        return super.makeMessage(channel).getTitle().clear().appendRaw(this.getClass().getSimpleName().replace("Exception", " Issue")).getMaker().getNote().append("Please check the help command for more information on how to use this command.").getMaker().withColor(COLOR);
    }
}
