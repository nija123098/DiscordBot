package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.configs.DisabledCommandsConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class GlobalDisableCommand extends AbstractCommand {
    public GlobalDisableCommand() {
        super("globaldisable", ModuleLevel.BOT_ADMINISTRATIVE, "gdisable, gd", null, "Disables commands globally");
    }
    @Command
    public void command(@Argument Boolean enable, @Argument AbstractCommand command, MessageMaker maker){
        if (command.equals(this)){
            maker.append("Ehe, so just because you are a " + this.getBotRole().name() + " you thought that I wouldn't have a check for your stupidity?");
            return;
        }
        ConfigHandler.alterSetting(DisabledCommandsConfig.class, GlobalConfigurable.GLOBAL, strings -> {
            if (enable) strings.remove(command.getName());
            else strings.add(command.getName());
            maker.appendRaw("Disabled: " + strings.toString());
        });
    }
}
