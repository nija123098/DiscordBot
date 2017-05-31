package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", ModuleLevel.ADMINISTRATIVE, "cfg", null, "Gets information on config values");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true) C configurable, User user, Guild guild, MessageMaker helper){
        if (configurable == null) configurable = (C) (guild == null ? user : guild);
        C finalConfigurable = configurable;
        ConfigHandler.getConfigs().stream().filter(config -> finalConfigurable.getConfigLevel().isAssignableFrom(config.getConfigLevel())).filter(config -> BotRole.hasRequiredRole(config.getBotRole(), user, guild)).forEach(config -> {
            helper.getNewFieldPart().getValue().append(config.getName()).getFieldPart().getValue().append(ConfigHandler.getExteriorSetting(config.getName(), finalConfigurable));// find a casting way to do this
        });
    }
    @Override
    public ModuleLevel getModule(){
        return ModuleLevel.ADMINISTRATIVE;
    }
}