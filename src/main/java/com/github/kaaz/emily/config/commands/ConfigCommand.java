package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", ModuleLevel.ADMINISTRATIVE, null, null, "Gets information on config values");
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
