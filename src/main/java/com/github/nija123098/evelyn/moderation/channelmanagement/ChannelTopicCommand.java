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

    public ChannelTopicCommand() {
        super(ChannelCommand.class,"topic", "ctopic", null, null, "Change the topic of a channel");
    }

    @Command
    public void command(@Argument(optional = true) Channel channel, @Argument String newTopic, Channel invokeChannel, MessageMaker maker) {
        maker.getTitle().appendRaw("Channel Topic Change").getMaker().withTimestamp(System.currentTimeMillis());
        try {
            if (channel == null) channel = invokeChannel;
            String previous = channel.getTopic();
            if (previous.equals("")) previous = "no prior topic set";
            if (!previous.equals(newTopic)) {
                channel.changeTopic(newTopic);
                maker.getNewFieldPart().withBoth("Previous Topic", previous);
                maker.getNewFieldPart().withBoth("Current Topic", newTopic);
            } else {
                maker.appendRaw("No change performed, topics were the same");
            }
            maker.getNote().appendRaw("Channel: " + channel.getID());
        } catch (PermissionsException e) {
            throw new PermissionsException("I could not change the topic for the `" + channel.getName() + "` channel, check your discord permissions to ensure I have permission to edit that channel.");
        }

    }
}