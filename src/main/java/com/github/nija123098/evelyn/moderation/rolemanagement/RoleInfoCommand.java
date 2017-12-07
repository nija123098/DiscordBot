package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleInfoCommand extends AbstractCommand {

	//constructor
	public RoleInfoCommand() {
		super(RoleCommand.class, "info", "rinfo", null, null, "Get info about a role");
	}

	@Command
	public void command(@Argument Role role, Guild guild, MessageMaker maker) {

		maker.getTitle().appendRaw(role.getName());
		maker.withColor(role);
		if (role.getPermissions().contains(DiscordPermission.ADMINISTRATOR)) maker.appendRaw(EmoticonHelper.getChars("oncoming_police_car", false) + " Administrator");
		maker.getHeader().appendRaw("\u200b");
		maker.getNewFieldPart().withInline(true).withBoth("Role Position", "" + (guild.getRoles().size() - role.getPosition()));
		maker.getNewFieldPart().withInline(true).withBoth("Users", "" + role.getUsers().size());
		maker.getNewFieldPart().withInline(true).withBoth("ID", role.getID());
		maker.getNewFieldPart().withInline(true).withBoth("Mentionable", "" + role.isMentionable());
		maker.getNewFieldPart().withInline(true).withBoth("Hoisted", "" + role.isHoisted());
		maker.getNewFieldPart().withInline(true).withBoth("Color", "R: " + role.getColor().getRed() + " G: " + role.getColor().getGreen() + " B: " + role.getColor().getBlue() +
																			   "\nHex: #" + Integer.toHexString(role.getColor().getRed()) + Integer.toHexString(role.getColor().getGreen()) + Integer.toHexString(role.getColor().getBlue()));
		maker.getNewFieldPart().withInline(true).withBoth("Permissions", FormatHelper.makeRolePermissionsTable(role));
	}
}