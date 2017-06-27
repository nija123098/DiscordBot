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
import com.github.kaaz.emily.util.LogicHelper;

import java.awt.*;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class ColorCommand extends AbstractCommand {
    public ColorCommand() {
        super("color", ModuleLevel.HELPER, null, null, "Displays information on a color");
    }
    @Command
    public void command(MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Color color, @Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument(optional = true, replacement = ContextType.NONE) User user, @Context(softFail = true) Guild guild) {
        if (!LogicHelper.oneNotNull(color, role, user)) throw new ArgumentException("Please provide either a color or a role or a user");
        if (user != null && guild == null) throw new ContextException("To check a user's color you must be in a guild");
        if (user != null) color = user.getRolesForGuild(guild).get(0).getColor();
        if (role != null) color = role.getColor();
        maker.withColor(color);
        maker.getNewFieldPart().withBoth("Hex", "#" + Integer.toHexString(color.getRGB()).toUpperCase());
        maker.getNewFieldPart().withBoth("RGB - Integer", color.getRed() + " " + color.getGreen() + " " + color.getBlue());
        float[] floats = color.getRGBComponents(null);
        maker.getNewFieldPart().withBoth("RGB - Float", floats[0] + " " + floats[1] + " " + floats[2]);
        maker.getNewFieldPart().withBoth("Integer", color.getRGB() + "");
    }
}
