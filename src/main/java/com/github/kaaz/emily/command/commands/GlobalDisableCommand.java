package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.configs.DisabledCommandsConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

import java.util.Set;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class GlobalDisableCommand extends AbstractCommand {
    public GlobalDisableCommand() {
        super("gdisable", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Disables commands globally");
    }
    @Command
    public void command(@Argument Boolean enable, @Argument AbstractCommand command, MessageMaker maker){
        Set<String> strings = ConfigHandler.getSetting(DisabledCommandsConfig.class, GlobalConfigurable.GLOBAL);
        if (enable) strings.remove(command.getName());
        else strings.add(command.getName());
        ConfigHandler.setSetting(DisabledCommandsConfig.class, GlobalConfigurable.GLOBAL, strings);
        maker.append(strings.toString());
    }
}
