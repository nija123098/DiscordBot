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
public class DeleteRoleCommand extends AbstractCommand {

	//constructor
	public DeleteRoleCommand() {
		super(RoleCommand.class, "delete", "delrole", null, null, "delete a role from the server");
	}

	@Command
	public void command(@Argument Role role, MessageMaker maker) {
		try {
			String roleName = role.getName();
			role.delete();
			maker.mustEmbed().appendRaw(roleName + " deleted successfully");
		} catch (PermissionsException e) {
			throw new PermissionsException("I do not have permission to edit that role, check your discord permissions");
		}
	}
}