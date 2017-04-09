package com.github.kaaz.emily.command.commands.config;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class ConfigGetCommand extends AbstractCommand {
    public ConfigGetCommand(ConfigCommand command) {
        super(command, "get", null, BotRole.USER, null, null, null);
    }
    @Command
    public <T extends Configurable> void command(@Convert AbstractConfig<?, T> config, @Convert T target, MessageMaker helper, String arg){
        if (!config.getConfigLevel().isAssignableFrom(target.getConfigLevel())){
            throw new ArgumentException("The command and the configurable are not of the same type");
        }
        helper.appendRaw(config.getExteriorValue(target));
    }
}
