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
public class ChannelNameCommand extends AbstractCommand {

	//constructor
	public ChannelNameCommand() {
		super(ChannelCommand.class,"name", "cname", null, null, "Change the channel name");
	}

	@Command
	public void command(@Argument(optional = true) Channel channel, @Argument String newName, Channel invokeChannel, MessageMaker maker) {
		maker.mustEmbed();
		maker.getTitle().appendRaw("Channel Name Change");
		maker.getHeader().appendRaw("\u200b");
		try {
			if (channel != null) {
				String previous = channel.getName();
				if (!previous.equals(newName.replace(' ', '_'))) {
					channel.changeName(newName.replace(' ', '_'));
					maker.getNewFieldPart().withInline(false).withBoth("Previous", String.valueOf(previous));
					maker.getNewFieldPart().withInline(false).withBoth("New", channel.mention());
				} else {
					maker.appendRaw("No change performed, names were the same");
				}
			} else {
				String previous = invokeChannel.getName();
				if (!previous.equals(newName.replace(' ', '_'))) {
					invokeChannel.changeName(newName.replace(' ', '_'));
					maker.getNewFieldPart().withInline(false).withBoth("Previous", String.valueOf(previous));
					maker.getNewFieldPart().withInline(false).withBoth("New", invokeChannel.mention());
				} else {
					maker.appendRaw("No change performed, names were the same");
				}
			}
		} catch (PermissionsException e) {
            assert channel != null;
            throw new PermissionsException("I could not rename the `" + channel.getName() + "` channel, check your discord permissions to ensure I have permission to edit that channel.");
		}

	}
}