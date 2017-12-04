package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.ContextException;

import java.awt.*;
import java.util.Objects;

import static com.github.nija123098.evelyn.command.ContextType.NONE;
import static com.github.nija123098.evelyn.command.ModuleLevel.HELPER;
import static java.lang.Integer.toHexString;
import static java.util.stream.Stream.of;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ColorCommand extends AbstractCommand {
    public ColorCommand() {
        super("color", HELPER, "colour", null, "Displays information on a colour");
    }

    @Command
    public void command(MessageMaker maker, @Argument(optional = true, replacement = NONE) Color color, @Argument(optional = true, replacement = NONE) Role role, @Argument(optional = true, replacement = NONE) User user, @Context(softFail = true) Guild guild) {
        if (of(color, role, user).filter(Objects::nonNull).count() != 1)
            throw new ArgumentException("Please provide either a hex, float, or integer representation, a role, a user, or a color name");
        if (user != null && guild == null) throw new ContextException("To check a user's color you must be in a guild");
        if (user != null) role = user.getRolesForGuild(guild).get(0);
        if (role != null) color = role.getColor();
        maker.withColor(color);
        maker.getNewFieldPart().withBoth("Hex", "#" + toHexString(color.getRGB()).toUpperCase());
        maker.getNewFieldPart().withBoth("RGB - Integer", color.getRed() + " " + color.getGreen() + " " + color.getBlue());
        float[] floats = color.getRGBComponents(null);
        maker.getNewFieldPart().withBoth("RGB - Float", floats[0] + " " + floats[1] + " " + floats[2]);
        maker.getNewFieldPart().withBoth("Integer", color.getRGB() + "");
    }
}
