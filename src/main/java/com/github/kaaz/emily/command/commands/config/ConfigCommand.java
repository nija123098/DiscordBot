package com.github.kaaz.emily.command.commands.config;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super(null, "config", ModuleLevel.ADMINISTRATIVE, BotRole.USER, null, null, null);
    }
    @Command
    public void command(MessageMaker helper){
        helper.appendContent("This will do something eventually");
    }
}
