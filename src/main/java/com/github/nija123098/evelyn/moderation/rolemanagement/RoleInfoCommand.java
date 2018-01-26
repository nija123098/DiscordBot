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

    public RoleInfoCommand() {
        super(RoleCommand.class, "info", "rinfo", null, null, "Get info about a role");
    }

    @Command
    public void command(@Argument Role role, Guild guild, MessageMaker maker) {

        maker.getTitle().appendRaw(role.getName());
        maker.withColor(role);
        if (role.getPermissions().contains(DiscordPermission.ADMINISTRATOR)) maker.appendRaw(EmoticonHelper.getChars("oncoming_police_car", false) + " Administrator");
        maker.getHeader().appendRaw("\u200b");
        addFieldPart(maker, "Role Position", "" + (guild.getRoles().size() - role.getPosition()));
        addFieldPart(maker, "Users", "" + role.getUsers().size());
        addFieldPart(maker, "ID", role.getID());
        addFieldPart(maker, "Mentionable", "" + role.isMentionable());
        addFieldPart(maker, "Hoisted", "" + role.isHoisted());
        addFieldPart(maker, "Color", "RGB: " + role.getColor().getRed() + ", " + role.getColor().getGreen() + ", " + role.getColor().getBlue() +
                "\nHex: #" + (Integer.toHexString(role.getColor().getRed()).length() == 1 ? "0" + Integer.toHexString(role.getColor().getRed()) : Integer.toHexString(role.getColor().getRed())) + (Integer.toHexString(role.getColor().getGreen()).length() == 1 ? "0" + Integer.toHexString(role.getColor().getGreen()) : Integer.toHexString(role.getColor().getGreen())) + (Integer.toHexString(role.getColor().getBlue()).length() == 1 ? "0" + Integer.toHexString(role.getColor().getBlue()) : Integer.toHexString(role.getColor().getBlue())));
        addFieldPart(maker, "Permissions", FormatHelper.makeRolePermissionsTable(role));
    }

    private static void addFieldPart(MessageMaker maker, String title, String value) {
        maker.getNewFieldPart().withInline(true).withBoth(title, value);
    }
}