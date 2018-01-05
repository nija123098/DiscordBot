package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class UserPermissionsCommand extends AbstractCommand {

    //constructor
    public UserPermissionsCommand() {
        super("user permissions", ModuleLevel.ADMINISTRATIVE, null, null, "Get a detailed view of a users permissions");
    }

    @Command
    public void command(@Argument User user, Guild guild, MessageMaker maker) {
        maker.mustEmbed();
        try {
            maker.getTitle().appendRaw(user.getDisplayName(guild));
            maker.getNewFieldPart().withBoth("User ID", user.getID());
            maker.getNewFieldPart().withInline(false).withBoth("Permissions", FormatHelper.makeUserPermissionsTable(user, guild, false));
        } catch (PermissionsException e) {
            throw new PermissionsException("I do not have permission to access the permissions of that user, ensure that my role is higher than theirs in the discord hierarchy");
        }
    }
}