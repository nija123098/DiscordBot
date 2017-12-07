package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.PermissionsException;

import java.awt.*;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleColorCommand extends AbstractCommand {

	//constructor
	public RoleColorCommand() {
		super(RoleCommand.class, "color", "rcolour, role colour, rcolor", null, null, "change the color of a role (colour is supported)");
	}

	@Command
	public void command(@Argument Role role, @Argument(info = "RGB/HEX") String color, MessageMaker maker) {
		maker.mustEmbed();
		try {
			if (color.split(" ").length == 3) {
				String[] rgb = color.split(" ");
				role.changeColor(new Color(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
				maker.appendRaw("Changed the color of `" + role.getName() + "`");
				maker.withColor(new Color(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
			} else if (color.matches("(#[0-a]{6})")) {
				role.changeColor(new Color(Integer.valueOf(color.substring(1, 3), 16), Integer.valueOf(color.substring(3, 5), 16), Integer.valueOf(color.substring(5, 7), 16)));
				maker.appendRaw("Changed the color of `" + role.getName() + "`");
				maker.withColor(new Color(Integer.valueOf(color.substring(1, 3), 16), Integer.valueOf(color.substring(3, 5), 16), Integer.valueOf(color.substring(5, 7), 16)));
			} else {
				maker.appendRaw("Please format your message using RGB e.g `role color roleName 255 0 0` or Hexadecimal e.g `role color roleName #FF000`");
			}
		} catch (PermissionsException e) {
			throw new PermissionsException("I do not have permission to edit that role, check your discord permissions");
		} catch (NumberFormatException e) {
			throw new ArgumentException("Please enter your RGB colors as valid integers");
		}

	}
}