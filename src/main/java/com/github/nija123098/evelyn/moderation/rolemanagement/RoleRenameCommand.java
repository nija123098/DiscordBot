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
public class RoleRenameCommand extends AbstractCommand {

    public RoleRenameCommand() {
        super(RoleCommand.class, "rename", "rname", null, null, "Rename a role in the server");
    }

    @Command
    public void command(@Argument Role role, @Argument String newName, MessageMaker maker) {
        try {
            String previous = role.getName();
            role.changeName(newName);
            maker.appendRaw("Successfully renamed the role `" + previous + "` to `" + role.getName() + "`");
        } catch (PermissionsException e) {
            throw new PermissionsException("I could not rename the `" + role.getName() + "` role, check your discord permissions to ensure my role is higher than the role I'm trying to rename");
        }
    }
}