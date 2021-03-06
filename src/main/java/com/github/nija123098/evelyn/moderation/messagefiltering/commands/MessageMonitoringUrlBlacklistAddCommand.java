package com.github.nija123098.evelyn.moderation.messagefiltering.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitor;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringUrlBlacklist;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringUrlBlacklistAddCommand extends AbstractCommand {
    public MessageMonitoringUrlBlacklistAddCommand() {
        super(MessageMonitoringUrlBlacklistCommand.class, "add", null, null, "a", "Adds a filtering level");
    }
    @Command
    public static void command(Guild guild, Channel channel, @Argument String domain, MessageMaker maker) {
        ConfigHandler.alterSetting(MessageMonitoringUrlBlacklist.class, guild, urls -> urls.add(domain.toLowerCase()));
        MessageMonitor.recalculate(guild);
        MessageMonitoringUrlBlacklistCommand.command(guild, channel, null, maker);
    }
}
