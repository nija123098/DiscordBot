package com.github.nija123098.evelyn.config.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.LanguageHelper;

/**
 * Made by nija123098 on 4/1/2017.
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", BotRole.USER, ModuleLevel.ADMINISTRATIVE, "cfg", null, "Gets information on config values");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true, info = "config/user/channel/role") C configurable, @Argument String s, User user, @Context(softFail = true) Guild guild, MessageMaker maker, @Context(softFail = true) Message message){
        if (configurable != null || s == null || s.isEmpty()){
            if (configurable == null) configurable = (C) (guild == null ? user : guild);
            C finalConfigurable = configurable;
            maker.getAuthorName().appendRaw(LanguageHelper.makePossessive(configurable.getName()) + " ").append(" Settings");
            if (configurable instanceof User || configurable instanceof Guild) maker.withAuthorIcon(configurable instanceof User ? ((User) configurable).getAvatarURL() : ((Guild) configurable).getIconURL());
            maker.getNote().append("To view " + (configurable instanceof User ? "server" : "user") + " settings use this command in a " + (configurable instanceof User ? "server" : "DM with me"));
            ConfigHandler.getConfigs(configurable.getClass()).stream().filter(config -> config.getBotRole().hasRequiredRole(user, guild)).filter(AbstractConfig::isNormalViewing).forEach(config -> maker.getNewFieldPart().withBoth(config.getName(), ConfigHandler.getExteriorSetting(config.getName(), finalConfigurable)));
        }else maker.append("Please use ").appendRaw("config get ").append("or").appendRaw(" config set").append(" but you can do !cfg [user/guild] to show all configs now.");
    }
}
