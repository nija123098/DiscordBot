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
public class RemoveRoleCommand extends AbstractCommand {

    public RemoveRoleCommand() {
        super(RoleCommand.class, "remove", null, null, null, "remove a role from a user");
    }

    @Command
    public void command(@Argument Role role, @Argument User user, Guild guild, MessageMaker maker) {
        maker.mustEmbed();
        try {
            user.removeRole(role);
            maker.appendRaw("Successfully removed the role " + role.getName() + " from " + user.getDisplayName(guild));
        } catch (PermissionsException e) {
            throw new PermissionsException("I'm could not remove the " + role.getName() + " role from " + user.getDisplayName(guild) + ", check your permissions and ensure my role is higher than the " + role.getName() + " role.");
        }
    }
}