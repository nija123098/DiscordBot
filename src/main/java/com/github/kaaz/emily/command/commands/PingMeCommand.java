package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingMeCommand extends AbstractCommand {
    public PingMeCommand(PingCommand command) {
        super(command, "me", null, null, "re", null, "mi");
    }
    @Command
    public void command(MessageHelper helper, @Convert User user){
        helper.appendTranslation("ping").withDM();
    }
}
