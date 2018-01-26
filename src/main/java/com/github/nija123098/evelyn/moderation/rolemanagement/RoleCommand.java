package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleCommand extends AbstractCommand {

    public RoleCommand() {
        super("role", ModuleLevel.ADMINISTRATIVE, null, null, "displays info about a role, or what the bot can edit");
    }

    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Role role, MessageMaker maker, Guild guild) {
        String prefix = ConfigHandler.getSetting(GuildPrefixConfig.class, guild);
        maker.mustEmbed();
        if (role == null) {
            if (DiscordClient.getOurUser().getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_ROLES)) {
                maker.getTitle().appendRaw("I can perform the following changes to roles in this server:");
                maker.appendRaw("Change role name\nChange role colour\nToggle role hoist\nClear all roles from a user\nRemove x role from a user\nAdd x role to a user\nDelete role\nGet role list for server\nGet role list of roles with zero users\nGet detailed role info");
                maker.getFooter().appendRaw("use `" + prefix + "help role` to see all the commands");
            } else {
                maker.appendRaw("I'm lacking the `manage roles` permission in your server to be able to perform any role management commands.");
            }
        } else {
            maker.getTitle().appendRaw(role.getName());
            maker.withColor(role);
            if (role.getPermissions().contains(DiscordPermission.ADMINISTRATOR)) maker.appendRaw(EmoticonHelper.getChars("oncoming_police_car", false) + " Administrator");
            maker.getHeader().appendRaw("\u200b");
            maker.getNewFieldPart().withInline(true).withBoth("Role Position", "" + (guild.getRoles().size() - role.getPosition()));
            maker.getNewFieldPart().withInline(true).withBoth("Users", "" + role.getUsers().size());
            maker.getNewFieldPart().withInline(true).withBoth("ID", role.getID());
            maker.getNewFieldPart().withInline(true).withBoth("Mentionable", "" + role.isMentionable());
            maker.getNewFieldPart().withInline(true).withBoth("Hoisted", "" + role.isHoisted());
            maker.getNewFieldPart().withInline(true).withBoth("Color", "RGB: " + role.getColor().getRed() + ", " + role.getColor().getGreen() + ", " + role.getColor().getBlue() +
                    "\nHex: #" + (Integer.toHexString(role.getColor().getRed()).length() == 1 ? "0" + Integer.toHexString(role.getColor().getRed()) : Integer.toHexString(role.getColor().getRed())) + (Integer.toHexString(role.getColor().getGreen()).length() == 1 ? "0" + Integer.toHexString(role.getColor().getGreen()) : Integer.toHexString(role.getColor().getGreen())) + (Integer.toHexString(role.getColor().getBlue()).length() == 1 ? "0" + Integer.toHexString(role.getColor().getBlue()) : Integer.toHexString(role.getColor().getBlue())));
            maker.getNewFieldPart().withInline(true).withBoth("Permissions", FormatHelper.makeRolePermissionsTable(role));
        }
    }
}