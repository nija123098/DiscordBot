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
public class ChannelRenameCommand extends AbstractCommand {

    public ChannelRenameCommand() {
        super(ChannelCommand.class,"name", "cname", null, null, "Change the channel name");
    }

    @Command
    public void command(@Argument(optional = true) Channel channel, @Argument String newName, Channel invokeChannel, MessageMaker maker) {
        try {
            if (channel == null) channel = invokeChannel;
            String previous = channel.getName();
            if (!previous.equals(newName.replace(' ', '_'))) {
                channel.changeName(newName.replace(' ', '_'));
                maker.appendRaw("Successfully renamed the ").appendEmbedLink(previous,"").append(" channel to " + channel.mention());
            } else {
                maker.appendRaw("No change performed, names were the same");
            }
        } catch (PermissionsException e) {
            throw new PermissionsException("I could not rename the " + channel.mention() + " channel, check your discord permissions to ensure I have permission to edit that channel.");
        }

    }
}