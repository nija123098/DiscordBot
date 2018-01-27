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

    public DeleteRoleCommand() {
        super(RoleCommand.class, "delete", "delrole", null, null, "Delete a role from the server");
    }

    @Command
    public void command(@Argument Role role, MessageMaker maker) {
        try {
            String roleName = role.getName();
            role.delete();
            maker.mustEmbed().appendRaw(roleName + " deleted successfully");
        } catch (PermissionsException e) {
            throw new PermissionsException("I could not delete the `" + role.getName() + "` role, check your discord permissions to ensure my role is higher than the role I'm trying to delete");
        }
    }
}