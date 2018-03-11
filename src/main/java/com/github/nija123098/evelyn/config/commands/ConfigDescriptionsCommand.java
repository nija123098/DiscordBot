package com.github.nija123098.evelyn.config.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ConfigDescriptionsCommand extends AbstractCommand {
    public ConfigDescriptionsCommand() {
        super(ConfigCommand.class, "descriptions", "cfginfo, cfgdesc", null, null, "Displays what a config does");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true, replacement = ContextType.NONE, info = "the configurable to get descriptions for") C configurable, @Argument(optional = true, info = "the config to get descriptions of", replacement = ContextType.NONE) AbstractConfig<?, C> config, @Context(softFail = true) Guild guild, User user, MessageMaker maker) {
        if (config == null && configurable == null) configurable = (C) (guild == null ? user : guild);
        if (configurable != null) {
            maker.getTitle().append("Config Descriptions for " + configurable.getName());
            ConfigHandler.getConfigs(configurable.getClass()).forEach(abstractConfig -> maker.getNewListPart().appendRaw("**" + abstractConfig.getName() + "**\n").append(abstractConfig.getDescription() + "\n"));
        }else{
            maker.getTitle().appendRaw(config.getName());
            maker.append(config.getDescription());
        }
    }
}
