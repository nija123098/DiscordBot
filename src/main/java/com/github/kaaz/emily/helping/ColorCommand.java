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
        super("colour", ModuleLevel.HELPER, "color", null, "Displays information on a colour");
    }
    @Command
    public void command(MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Color colour, @Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument(optional = true, replacement = ContextType.NONE) User user, @Context(softFail = true) Guild guild) {
        if (Stream.of(colour, role, user).filter(Objects::nonNull).count() != 1) throw new ArgumentException("Please provide either a hex, float, or integer representation, a role, a user, or a colour name");
        if (user != null && guild == null) throw new ContextException("To check a user's colour you must be in a guild");
        if (user != null) colour = user.getRolesForGuild(guild).get(0).getColor();
        if (role != null) colour = role.getColor();
        maker.withColor(colour);
        maker.getNewFieldPart().withBoth("Hex", "#" + Integer.toHexString(colour.getRGB()).toUpperCase());
        maker.getNewFieldPart().withBoth("RGB - Integer", colour.getRed() + " " + colour.getGreen() + " " + colour.getBlue());
        float[] floats = colour.getRGBComponents(null);
        maker.getNewFieldPart().withBoth("RGB - Float", floats[0] + " " + floats[1] + " " + floats[2]);
        maker.getNewFieldPart().withBoth("Integer", colour.getRGB() + "");
    }
}
