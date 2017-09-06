package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

public class ConfigDescriptionsCommand extends AbstractCommand {
    public ConfigDescriptionsCommand() {
        super(ConfigCommand.class, "descriptions", "info", null, null, "Displays what a config does");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true, info = "the config to get descriptions of") C configurable, @Context(softFail = true) Guild guild, User user, MessageMaker maker){
        if (configurable == null) configurable = (C) (guild == null ? user : guild);
        ConfigHandler.getConfigs(configurable.getClass()).forEach(abstractConfig -> maker.getNewListPart().appendRaw(abstractConfig.getName() + " - ").append(abstractConfig.getName()));
    }
}
