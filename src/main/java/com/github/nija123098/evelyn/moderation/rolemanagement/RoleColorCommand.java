package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exception.PermissionsException;

import java.awt.*;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleColorCommand extends AbstractCommand {

    public RoleColorCommand() {
        super(RoleCommand.class, "color", "rcolour, role colour, rcolor", null, null, "Change the color of a role (colour is supported)");
    }

    @Command
    public void command(@Argument Role role, @Argument(info = "RGB/HEX") Color color, MessageMaker maker) {
        maker.mustEmbed();
        try {
            role.changeColor(color);
            maker.appendRaw("Changed the colour of `" + role.getName() + "`");
            maker.withColor(color);
        } catch (PermissionsException e) {
            throw new PermissionsException("I could not change the colour of the `" + role.getName() + "` role, check your discord permissions to ensure my role is higher than the role I'm trying to change the colour of");
        }
    }
}