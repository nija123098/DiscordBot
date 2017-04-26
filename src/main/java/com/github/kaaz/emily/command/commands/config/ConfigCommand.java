package com.github.kaaz.emily.command.commands.config;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", null, null);
    }
    @Command
    public void command(MessageMaker helper){
        helper.append("This will do something eventually");
    }
    @Override
    public ModuleLevel getModule(){
        return ModuleLevel.ADMINISTRATIVE;
    }
}
