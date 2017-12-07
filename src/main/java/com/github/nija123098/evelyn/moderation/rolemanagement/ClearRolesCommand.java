package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.PermissionsException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ClearRolesCommand extends AbstractCommand {

	//constructor
	public ClearRolesCommand() {
		super(RoleCommand.class, "clear", null, null, null, "remove all roles from a user");
	}

	@Command
	public void command(@Argument User user, Guild guild, MessageMaker maker) {
		try {
			user.getRolesForGuild(guild).forEach(user::removeRole);
			maker.mustEmbed().appendRaw("successfully removed all roles from " + user.getDisplayName(guild));
		} catch (PermissionsException e) {
			throw new PermissionsException("I can't edit that users roles");
		}
	}
}