package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.PermissionsException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleAddCommand extends AbstractCommand {

	//constructor
	public RoleAddCommand() {
		super(RoleCommand.class, "add", "radd", null, null, "give a user a role");
	}

	@Command
	public void command(@Argument User user, @Argument Role role, Guild guild, MessageMaker maker) {
		maker.mustEmbed();
		try {
			user.addRole(role);
			maker.appendRaw("Successfully added the `" + role.getName() + "` role to " + user.getDisplayName(guild));
		} catch (PermissionsException e) {
			throw new PermissionsException("Could not add the " + role.getName() + " role to " + user.getDisplayName(guild) + ", check your discord permissions to ensure my role is higher than the role I'm trying to add.");
		}
	}
}