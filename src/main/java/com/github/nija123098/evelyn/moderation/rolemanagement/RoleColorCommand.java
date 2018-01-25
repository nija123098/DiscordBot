package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

import java.awt.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RoleColorCommand extends AbstractCommand {
    public RoleColorCommand() {
        super(RoleCommand.class, "color", "rcolour, role colour, rcolor", null, null, "Changes the color (or colour if you spell it wrong) of a role.");
    }
    @Command
    public void command(@Argument Role role, @Argument(info = "RGB/HEX/INT") Color color, MessageMaker maker) {
        role.changeColor(color);
        maker.mustEmbed().withColor(color).append("Color updated!");
    }
}