package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleRemoveCommand extends AbstractCommand {

    public RoleRemoveCommand() {
        super(RoleCommand.class, "remove", "rremove", null, null, "Remove a role from a user");
    }

    @Command
    public void command(@Argument Role role, @Argument(optional = true, replacement = ContextType.NONE) User user, @Argument(optional = true) Role targetRole, Guild guild, MessageMaker maker) {
        maker.mustEmbed();
        if (user != null) {
            try {
                user.removeRole(role);
                maker.appendRaw("Successfully removed the role " + FormatHelper.embedLink(role.getName(),"") + " from " + user.getDisplayName(guild));
            } catch (PermissionsException e) {
                throw new PermissionsException("I'm could not remove the " + FormatHelper.embedLink(role.getName(),"") + " role from " + user.getDisplayName(guild) + ", check your permissions and ensure my role is higher than the " + role.getName() + " role.");
            }
        } else if (targetRole != null) {
            List<User> users = targetRole.getUsers().stream().filter(user1 -> user1.getRolesForGuild(guild).contains(targetRole)).collect(Collectors.toList());
            try {
                users.forEach(user1 -> user1.removeRole(role));
                maker.appendRaw("Successfully removed the " + FormatHelper.embedLink(role.getName(),"") + " role from the users with the " + FormatHelper.embedLink(targetRole.getName(),"") + " role");
            } catch (PermissionsException e) {
                throw new PermissionsException("I could not remove the " + FormatHelper.embedLink(role.getName(),"") + " role from the users with the " + FormatHelper.embedLink(targetRole.getName(),"") + " role, check your discord permissions to ensure my role is higher than the role I'm trying to remove.");
            }
        }
    }
}