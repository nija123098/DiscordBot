package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

/**
 * The exception thrown when there is an internal
 * error that is the developer's fault.
 *
 * @author nija123098
 * @since 1.0.0
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

    @Override
    public MessageMaker makeMessage(Channel channel) {
        return super.makeMessage(channel).getFooter().append("The developers have been notified.\nOr use `@Evelyn bug` to send a bug report.").getMaker().getTitle().clear().appendRaw("Development Exception").getMaker();
    }
}
