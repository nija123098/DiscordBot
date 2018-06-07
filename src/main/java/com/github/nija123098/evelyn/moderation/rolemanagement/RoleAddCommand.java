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
public class RoleAddCommand extends AbstractCommand {

    public RoleAddCommand() {
        super(RoleCommand.class, "add", "radd", null, null, "Give a user or all users with a role a role");
    }

    @Command
    public void command(@Argument Role role, @Argument(optional = true, replacement = ContextType.NONE) User user, @Argument(optional = true, replacement = ContextType.NONE) Role targetRole, Guild guild, MessageMaker maker) {
        if (user != null) {
            try {
                user.addRole(role);
                maker.appendRaw("Successfully added the ").appendEmbedLink(role.getName(),"").append(" role to ").appendRaw(user.getDisplayName(guild));
            } catch (PermissionsException e) {
                throw new PermissionsException("I could not add the " + FormatHelper.embedLink(role.getName(),"") + " role to " + user.getDisplayName(guild) + ", check your discord permissions to ensure my role is higher than the role I'm trying to add.");
            }
        } else if (targetRole != null) {
            List<User> users = targetRole.getUsers().stream().filter(user1 -> user1.getRolesForGuild(guild).contains(targetRole)).collect(Collectors.toList());
            try {
                users.forEach(user1 -> user1.addRole(role));
                maker.appendRaw("Successfully added the ").appendEmbedLink(role.getName(), "").append(" role to the users with the ").appendEmbedLink(targetRole.getName(),"").append(" role");
            } catch (PermissionsException e) {
                throw new PermissionsException("I could not add the " + FormatHelper.embedLink(role.getName(),"") + " role to the users with the " + FormatHelper.embedLink(targetRole.getName(),"") + " role, check your discord permissions to ensure my role is higher than the role I'm trying to add.");
            }
        } else {
            maker.appendRaw("I found no user or role to apply the new role to.");
        }
    }
}