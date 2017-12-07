package com.github.nija123098.evelyn.moderation.rolemanagement;

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
public class RoleCommand extends AbstractCommand {

	//constructor
	public RoleCommand() {
		super("role", ModuleLevel.ADMINISTRATIVE, null, null, "supercommand for role management");
	}

	@Command
	public void command(MessageMaker maker, Guild guild) {
		String prefix = ConfigHandler.getSetting(GuildPrefixConfig.class, guild);
		maker.mustEmbed();
		if (DiscordClient.getOurUser().getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_ROLES)) {
			maker.getTitle().appendRaw("I can perform the following changes to roles in this server:");
			maker.appendRaw("Change role name\nChange role colour\nToggle role hoist\nClear all roles from a user\nRemove x role from a user\nAdd x role to a user\nDelete role\nGet detailed role info");
			maker.getFooter().appendRaw("use `" + prefix + "help role` to see a overview of how to use the commands");
		} else {
			maker.appendRaw("I'm lacking the `manage roles` permission in your server to be able to perform any role management commands.");
		}
	}
}