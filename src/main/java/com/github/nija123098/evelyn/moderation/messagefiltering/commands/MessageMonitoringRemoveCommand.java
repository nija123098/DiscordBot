package com.github.nija123098.evelyn.moderation.messagefiltering.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitor;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringAdditionsConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringExceptionsConfig;

public class MessageMonitoringRemoveCommand extends AbstractCommand {
    public MessageMonitoringRemoveCommand() {
        super(MessageMonitoringCommand.class, "add", null, null, null, "Adds a filtering level");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Channel channel, Guild guild, @Argument MessageMonitoringLevel level, MessageMaker maker){
        if (channel == null) ConfigHandler.alterSetting(MessageMonitoringConfig.class, guild, levels -> levels.remove(level));
        else {
            ConfigHandler.alterSetting(MessageMonitoringExceptionsConfig.class, channel, levels -> levels.add(level));
            ConfigHandler.alterSetting(MessageMonitoringAdditionsConfig.class, channel, levels -> levels.remove(level));
        }
        if (channel != null) MessageMonitor.recalculate(channel);
        MessageMonitoringCommand.command(channel, guild, maker);
    }
}
