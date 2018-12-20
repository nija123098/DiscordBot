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

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringAddCommand extends AbstractCommand {
    public MessageMonitoringAddCommand() {
        super(MessageMonitoringCommand.class, "add", null, null, "a", "Adds a filtering level");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Channel channel, Guild guild, @Argument MessageMonitoringLevel level, MessageMaker maker) {
        if (channel == null) ConfigHandler.alterSetting(MessageMonitoringConfig.class, guild, levels -> levels.add(level));
        else {
            ConfigHandler.alterSetting(MessageMonitoringExceptionsConfig.class, channel, levels -> levels.remove(level));
            ConfigHandler.alterSetting(MessageMonitoringAdditionsConfig.class, channel, levels -> levels.add(level));
        }
        if (channel != null) MessageMonitor.recalculate(channel);
        MessageMonitoringCommand.command(channel, guild, maker);
    }
}
