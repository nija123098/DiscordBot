package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleCreateCommand extends AbstractCommand {

    public RoleCreateCommand() {
        super(RoleCommand.class, "create", "rcreate", null, null, "Create a role");
    }

    @Command
    public void command(@Argument String name, Guild guild, MessageMaker maker) {
        maker.getTitle().appendRaw("Role creation");
        try {
            Role role = guild.createRole();
            role.changeName(name);
            maker.appendRaw("Successfully created the ").appendEmbedLink(name, "").appendRaw(" role.");
        } catch (PermissionsException e) {
            throw new PermissionsException("I could not create the " + FormatHelper.embedLink(name, "") + " role, check your discord permissions to ensure I have permission to manage roles.");
        }
    }
}