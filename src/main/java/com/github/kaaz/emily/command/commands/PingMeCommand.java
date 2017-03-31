package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingMeCommand extends AbstractCommand {
    public PingMeCommand(PingCommand command) {
        super(command, "me", null, null, "re", null, "mi");
    }
    @Command
    public void command(MessageHelper helper){
        helper.appendTranslation("ping").withDM();
    }
}
