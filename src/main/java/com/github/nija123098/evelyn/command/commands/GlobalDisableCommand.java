package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.configs.DisabledCommandsConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * @author nija123098
 * @since 1.0.0
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
