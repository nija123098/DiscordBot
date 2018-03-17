package com.github.nija123098.evelyn.moderation.channelmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelInfoCommand extends AbstractCommand {

    public ChannelInfoCommand() {
        super(ChannelCommand.class,"info", "cinfo", null, null, "Get detailed channel info");
    }

    @Command
    public void command(@Argument(optional = true) Channel channel, MessageMaker maker, Channel invokeChannel) {
        maker.mustEmbed();
        if (channel != null) {
            maker.getTitle().appendRaw(channel.getName());
            maker.appendRaw("\u200b");
            maker.getNewFieldPart().withInline(true).withBoth("Topic", ((channel.getTopic() == null || channel.getTopic().isEmpty()) ? "none" : channel.getTopic()));
            if (channel.getCategory() != null)maker.getNewFieldPart().withInline(true).withBoth("Category", channel.getCategory().getName());
            maker.getNewFieldPart().withInline(true).withBoth("ID", channel.getID());
            maker.getNewFieldPart().withInline(true).withBoth("NSFW", "" + channel.isNSFW());
            maker.getNewFieldPart().withInline(true).withBoth("Users with access", (channel.getUsersHere().size() <= 12 ? FormatHelper.makeUserTable(channel.getUsersHere(), 23, 2) : "" + channel.getUsersHere().size()));
        } else {
            maker.getTitle().appendRaw(invokeChannel.getName());
            maker.appendRaw("\u200b");
            maker.getNewFieldPart().withInline(true).withBoth("Topic", ((invokeChannel.getTopic() == null || invokeChannel.getTopic().isEmpty()) ? "none" : invokeChannel.getTopic()));
            if (invokeChannel.getCategory() != null)maker.getNewFieldPart().withInline(true).withBoth("Category", invokeChannel.getCategory().getName());
            maker.getNewFieldPart().withInline(true).withBoth("ID", invokeChannel.getID());
            maker.getNewFieldPart().withInline(true).withBoth("NSFW", "" + invokeChannel.isNSFW());
            maker.getNewFieldPart().withInline(true).withBoth("Users with access", (invokeChannel.getUsersHere().size() <= 12 ? FormatHelper.makeUserTable(invokeChannel.getUsersHere(), 23, 2) : "" + invokeChannel.getUsersHere().size()));
        }
    }
}