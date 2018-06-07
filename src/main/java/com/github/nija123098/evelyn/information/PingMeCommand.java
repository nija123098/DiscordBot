package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PingMeCommand extends AbstractCommand {
    public PingMeCommand() {
        super(PingCommand.class, "me", null, null, null, "Pings the user through DM");
    }
    @Command
    public void command(MessageMaker maker, Message message, User user) {
        maker.withChannel(user.getOrCreatePMChannel());
        PingCommand.command(maker, message);
    }
}
