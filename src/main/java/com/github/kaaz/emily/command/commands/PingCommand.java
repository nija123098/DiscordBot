package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.Command;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super(null, "ping", ModuleLevel.NONE, BotRole.USER, null, null, null);
    }
    @Command
    public void command(MessageHelper helper){
        helper.appendTranslation("ping");
    }
}
