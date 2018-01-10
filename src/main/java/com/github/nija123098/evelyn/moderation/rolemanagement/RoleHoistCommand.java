package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exception.PermissionsException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleHoistCommand extends AbstractCommand {

	//constructor
	public RoleHoistCommand() {
		super(RoleCommand.class, "hoist", "rhoist", null, null, "toggles whether a role is displayed separately");
	}

	@Command
	public void command(@Argument Role role, MessageMaker maker) {
		maker.mustEmbed();
		try {
			role.changeHoist(!role.isHoisted());
			if (role.isHoisted()) {
				maker.appendRaw("Successfully hoisted role " + role.getName());
			} else {
				maker.appendRaw("Successfully dehoisted role " + role.getName());
			}
		} catch (PermissionsException e) {
			throw new PermissionsException("I could not hoist the `" + role.getName() + "` role, check your discord permissions to ensure my role is higher than the role I'm trying to hoist");
		}

	}
}