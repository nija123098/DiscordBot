package com.github.kaaz.emily.command.commands.config;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class ConfigSetCommand extends AbstractCommand {
    public ConfigSetCommand(ConfigCommand command) {
        super(command, "set", null, null, null, null, null);
    }
    @Command
    public void command(@Convert AbstractConfig config, @Convert Configurable target, User user, Message message, Reaction reaction, String arg){// unfortunately conversion wouldn't work here since we don't have the result of the
        if (!config.getConfigLevel().isAssignableFrom(target.getConfigLevel())){
            throw new ArgumentException("The specified config can not be applied to that configurable");
        }
        ConfigHandler.setExteriorSetting(config.getClass(), target, arg);
    }
}
