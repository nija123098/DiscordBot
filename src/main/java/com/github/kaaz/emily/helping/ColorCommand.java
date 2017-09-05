package com.github.kaaz.emily.helping;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.ContextException;

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
