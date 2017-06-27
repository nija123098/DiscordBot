package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingMeCommand extends AbstractCommand {
    public PingMeCommand() {
        super(PingCommand.class, "me", null, null, null, "Pings the user through DM");
    }
    @Command
    public void command(MessageMaker helper){
        helper.append("ping").withDM();
    }
}
