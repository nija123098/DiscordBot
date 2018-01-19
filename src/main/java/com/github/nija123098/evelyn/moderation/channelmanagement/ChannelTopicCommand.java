package com.github.nija123098.evelyn.moderation.channelmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.PermissionsException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelTopicCommand extends AbstractCommand {

    //constructor
    public ChannelTopicCommand() {
        super(ChannelCommand.class,"topic", "ctopic", null, null, "change the topic of a channel");
    }

    @Command
    public void command(@Argument(optional = true) Channel channel, @Argument String newTopic, Channel invokeChannel, MessageMaker maker) {
        maker.mustEmbed();
        maker.getTitle().appendRaw("Channel Topic Change");
        maker.getHeader().appendRaw("\u200b");
        try {
            if (channel != null) {
                String previous = channel.getTopic();
                if (previous.equals("")) previous = "no prior topic set";
                if (!previous.equals(newTopic)) {
                    channel.changeTopic(newTopic);
                    maker.getNewFieldPart().withInline(false).withBoth("Previous", previous);
                    maker.getNewFieldPart().withInline(false).withBoth("New", newTopic);
                } else {
                    maker.appendRaw("No change performed, topics were the same");
                }
            } else {
                String previous = invokeChannel.getName();
                if (previous.equals("")) previous = "no prior topic set";
                if (!previous.equals(newTopic)) {
                    invokeChannel.changeTopic(newTopic);
                    maker.getNewFieldPart().withInline(false).withBoth("Previous", previous);
                    maker.getNewFieldPart().withInline(false).withBoth("New", newTopic);
                } else {
                    maker.appendRaw("No change performed, topics were the same");
                }
            }
        } catch (PermissionsException e) {
            assert channel != null;
            throw new PermissionsException("I could not change the topic for the `" + channel.getName() + "` channel, check your discord permissions to ensure I have permission to edit that channel.");
        }

    }
}