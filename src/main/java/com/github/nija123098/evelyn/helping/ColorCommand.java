package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.ContextException;

import java.awt.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class ColorCommand extends AbstractCommand {
    public ColorCommand() {
        super("color", ModuleLevel.HELPER, "colour", null, "Displays information on a colour");
    }
    @Command
    public void command(MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Color color, @Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument(optional = true, replacement = ContextType.NONE) User user, @Context(softFail = true) Guild guild) {
        if (Stream.of(color, role, user).filter(Objects::nonNull).count() != 1) throw new ArgumentException("Please provide either a hex, float, or integer representation, a role, a user, or a color name");
        if (user != null && guild == null) throw new ContextException("To check a user's color you must be in a guild");
        if (user != null) role = user.getRolesForGuild(guild).get(0);
        if (role != null) color = role.getColor();
        maker.withColor(color);
        maker.getNewFieldPart().withBoth("Hex", "#" + Integer.toHexString(color.getRGB()).toUpperCase());
        maker.getNewFieldPart().withBoth("RGB - Integer", color.getRed() + " " + color.getGreen() + " " + color.getBlue());
        float[] floats = color.getRGBComponents(null);
        maker.getNewFieldPart().withBoth("RGB - Float", floats[0] + " " + floats[1] + " " + floats[2]);
        maker.getNewFieldPart().withBoth("Integer", color.getRGB() + "");
    }
}
