package com.github.nija123098.evelyn.moderation.channelmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelCreateCommand extends AbstractCommand {

    public ChannelCreateCommand() {
        super(ChannelCommand.class, "create", "ccreate", null, null, "Create a channel");
    }

    @Command
    public void command(@Argument String channelName, Channel channel, Guild guild, MessageMaker maker) {
        maker.getTitle().appendRaw("Channel creation").getMaker().withTimestamp(System.currentTimeMillis());
        channelName = channelName.replace(' ', '_');
        if (channelName.matches("^[a-zA-Z0-9_ ]+$")) {
            if (channel.getCategory() != null) {
                try {
                    channel.getCategory().category().createChannel(channelName);
                } catch (PermissionsException e) {
                    throw new PermissionsException("I could not create the " + FormatHelper.embedLink(channelName,"") + " channel, check your discord permissions to ensure I have permission to manage channels.");
                }
                maker.appendRaw("Successfully created the " + channel.mention() + " channel in the ").appendEmbedLink(channel.getCategory().getName(),"").appendRaw(" category.");
            } else {
                try {
                    guild.createChannel(channelName);
                } catch (PermissionsException e) {
                    throw new PermissionsException("I could not create the `" + FormatHelper.embedLink(channelName,"") + "` channel, check your discord permissions to ensure I have permission to manage channels.");
                }
                maker.appendRaw("Successfully created the " + channel.mention() + " channel in the server.");
            }
        } else {
            throw new ArgumentException("Please use only alphanumeric characters [a-z, A-Z, 0-9, _] for your channel names");
        }
    }
}