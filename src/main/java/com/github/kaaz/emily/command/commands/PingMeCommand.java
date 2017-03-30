package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractSubCommand;
import com.github.kaaz.emily.command.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;

/**
 * Made by nija123098 on 3/29/2017.
 */
public class PingMeCommand extends AbstractSubCommand {
    public PingMeCommand() {
        super("me", null, "me", null, null);
    }
    @Command
    public void command(MessageHelper helper){
        helper.withDM().appendTranslation("me");
    }
}
