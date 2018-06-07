package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.PermissionsException;

import java.util.ArrayList;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleClearCommand extends AbstractCommand {

    public RoleClearCommand() {
        super(RoleCommand.class, "clear", "rclear", null, null, "Clear all roles from a user");
    }

    @Command
    public void command(@Argument User user, Guild guild, MessageMaker maker) {
        ArrayList<String> failedRoles = new ArrayList<>();
        for (Role role : user.getRolesForGuild(guild)) {
            try {
                user.removeRole(role);
            } catch (PermissionsException IGNORE) {
                failedRoles.add(role.getName());
            }
        }
        if (failedRoles.size() > 0) {
            StringBuilder builder = new StringBuilder();
            failedRoles.forEach(role -> builder.append("`").append(role).append("`\n"));
            throw new PermissionsException("I could not remove the following roles from " + user.getDisplayName(guild) + ":\n" + builder.toString() + "\ncheck your discord permissions to ensure my role is higher than the role I'm trying to remove.");
        } else {
            maker.append("Successfully removed all roles from ").appendRaw(user.getDisplayName(guild));
        }
    }
}