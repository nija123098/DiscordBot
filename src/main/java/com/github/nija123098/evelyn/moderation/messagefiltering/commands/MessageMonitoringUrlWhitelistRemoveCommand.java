package com.github.nija123098.evelyn.moderation.messagefiltering.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitor;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringUrlWhitelist;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringUrlWhitelistRemoveCommand extends AbstractCommand {
    public MessageMonitoringUrlWhitelistRemoveCommand() {
        super(MessageMonitoringUrlWhitelistCommand.class, "remove", null, null, "r", "Removes a filtering level");
    }
    @Command
    public void command(Guild guild, Channel channel, @Argument String domain, MessageMaker maker) {
        ConfigHandler.alterSetting(MessageMonitoringUrlWhitelist.class, guild, urls -> urls.remove(domain));
        MessageMonitor.recalculate(guild);
        MessageMonitoringUrlWhitelistCommand.command(maker, channel, null);
    }
}
