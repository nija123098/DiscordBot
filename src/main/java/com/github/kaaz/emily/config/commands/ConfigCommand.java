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
import com.github.kaaz.emily.util.LanguageHelper;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", BotRole.USER, ModuleLevel.ADMINISTRATIVE, "cfg", null, "Gets information on config values");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true) C configurable, @Argument String s, User user, @Context(softFail = true) Guild guild, MessageMaker helper, @Context(softFail = true) Message message){
        if (s == null || s.isEmpty()){
            if (configurable == null) configurable = (C) (guild == null ? user : guild);
            C finalConfigurable = configurable;
            helper.getAuthorName().appendRaw(LanguageHelper.makePossessive(configurable.getName()) + " ").append(" Settings");
            if (configurable instanceof User || configurable instanceof Guild) helper.withAuthorIcon(configurable instanceof User ? ((User) configurable).getAvatarURL() : ((Guild) configurable).getIconURL());
            helper.getNote().append("To view " + (configurable instanceof User ? "server" : "user") + " settings use this command in a " + (configurable instanceof User ? "server" : "DM with me"));
            ConfigHandler.getConfigs().stream().filter(config -> finalConfigurable.getConfigLevel().isAssignableFrom(config.getConfigLevel())).filter(config -> config.getBotRole().hasRequiredRole(user, guild)).filter(AbstractConfig::isNormalViewing).forEach(config -> {
                helper.getNewFieldPart().withBoth(config.getName(), ConfigHandler.getExteriorSetting(config.getName(), finalConfigurable));// find a casting way to do this
            });
        }else CommandHandler.attemptInvocation("cfg set", user, message, null);
    }
}
