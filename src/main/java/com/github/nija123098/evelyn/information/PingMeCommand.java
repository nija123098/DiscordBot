package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PingMeCommand extends AbstractCommand {
    public PingMeCommand() {
        super(PingCommand.class, "me", null, null, null, "Pings the user through DM");
    }
    @Command
    public void command(MessageMaker helper) {
        helper.append("ping").mustEmbed().withDM();
    }
}
