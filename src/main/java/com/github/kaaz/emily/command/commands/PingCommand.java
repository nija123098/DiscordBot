package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super(null, "ping", ModuleLevel.NONE, BotRole.USER, null, null, null);
    }
    @Command
    public void command(MessageMaker helper){
        helper.appendContent("red").withUserColor();
    }
}
