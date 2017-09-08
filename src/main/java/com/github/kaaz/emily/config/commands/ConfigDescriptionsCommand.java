package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

public class ConfigDescriptionsCommand extends AbstractCommand {
    public ConfigDescriptionsCommand() {
        super(ConfigCommand.class, "descriptions", "info, desc", null, null, "Displays what a config does");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true, replacement = ContextType.NONE, info = "the configurable to get descriptions for") C configurable, @Argument(optional = true, info = "the config to get descriptions of") AbstractConfig<?, C> config, @Context(softFail = true) Guild guild, User user, MessageMaker maker){
        if (config == null && configurable == null) configurable = (C) (guild == null ? user : guild);
        if (configurable != null){
            maker.getTitle().append("Config Descriptions for " + configurable.getName());
            ConfigHandler.getConfigs(configurable.getClass()).forEach(abstractConfig -> maker.getNewListPart().appendRaw("**" + abstractConfig.getName() + "**\n").append(abstractConfig.getDescription() + "\n"));
        }else{
            maker.getTitle().appendRaw(config.getName());
            maker.append(config.getDescription());
        }
    }
}
