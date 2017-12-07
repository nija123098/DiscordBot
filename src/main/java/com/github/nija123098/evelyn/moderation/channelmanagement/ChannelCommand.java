package com.github.nija123098.evelyn.moderation.channelmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelCommand extends AbstractCommand {

	//constructor
	public ChannelCommand() {
		super("channel", ModuleLevel.ADMINISTRATIVE, null, null, "supercommand for channel management");
	}

	@Command
	public void command(MessageMaker maker, Guild guild) {
		String prefix = ConfigHandler.getSetting(GuildPrefixConfig.class, guild);
		maker.mustEmbed();
		if (DiscordClient.getOurUser().getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_CHANNELS)) {
			maker.getTitle().appendRaw("I can perform the following changes to channels in this server");
			maker.appendRaw("Change channel name\nChange channel topic\nGet detailed channel info");
			maker.getFooter().appendRaw("use `" + prefix + "help channel` to see a overview of how to use the commands");
		}
	}
}