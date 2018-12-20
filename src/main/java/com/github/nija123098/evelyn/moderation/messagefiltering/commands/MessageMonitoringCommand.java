package com.github.nija123098.evelyn.moderation.messagefiltering.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitor;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringCommand extends AbstractCommand {
    public MessageMonitoringCommand() {
        super("messagemonitoring", ModuleLevel.ADMINISTRATIVE, "message monitoring, mm", null, "Displays a list of monitoring in effect currently");
    }
    @Command
    public static void command(@Argument(optional = true, replacement = ContextType.NONE) Channel channel, Guild guild, MessageMaker maker) {
        Set<MessageMonitoringLevel> levels = channel == null ? ConfigHandler.getSetting(MessageMonitoringConfig.class, guild) : MessageMonitor.getLevels(channel);
        if (levels.isEmpty()) maker.append("No monitoring is active here.  That's not very safe!");
        maker.getTitle().append("Message monitoring levels for " + (channel == null ? guild.getName() : channel.mention()));
        for (MessageMonitoringLevel level : MessageMonitoringLevel.values()) {
            maker.getNewFieldPart().getTitle().appendRaw(level.name()).getFieldPart().getValue().appendRaw(EmoticonHelper.getChars(levels.contains(level) ? "white_check_mark" : "x", false));
        }
    }
}
