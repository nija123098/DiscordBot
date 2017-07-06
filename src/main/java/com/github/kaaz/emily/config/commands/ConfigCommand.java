package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", BotRole.USER, ModuleLevel.ADMINISTRATIVE, "cfg", null, "Gets information on config values");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true) C configurable, String s, User user, Guild guild, MessageMaker helper, @Context(softFail = true) Message message){
        if (s == null){
            if (configurable == null) configurable = (C) (guild == null ? user : guild);
            C finalConfigurable = configurable;
            ConfigHandler.getConfigs().stream().filter(config -> finalConfigurable.getConfigLevel().isAssignableFrom(config.getConfigLevel())).filter(config -> config.getBotRole().hasRequiredRole(user, guild)).filter(AbstractConfig::isNormalViewing).forEach(config -> {
                helper.getNewFieldPart().withBoth(config.getName(), ConfigHandler.getExteriorSetting(config.getName(), finalConfigurable));// find a casting way to do this
            });
        }else CommandHandler.attemptInvocation("cfg set", user, message, null);
    }
}
