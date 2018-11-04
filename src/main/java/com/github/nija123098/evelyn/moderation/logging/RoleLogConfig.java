package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordRoleCreate;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordRoleDelete;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordRoleUpdate;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserRolesUpdateEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collection;
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleLogConfig extends AbstractConfig<Channel, Guild> {
    public RoleLogConfig() {
        super("role_edit_log", "Role Edit Log", ConfigCategory.LOGGING, (Channel) null, "Log role changes");
    }

    @EventListener
    public void handle(DiscordRoleCreate event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel);
        Logging.ROLE_CREATION.roleLog(maker, event.getRole());
    }

    @EventListener
    public void handle(DiscordRoleDelete event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel);
        Logging.ROLE_DELETION.roleLog(maker, event.getRole());
    }

    @EventListener
    public void handle(DiscordRoleUpdate event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        IRole oldRole = event.getOldRole();
        IRole newRole = event.getNewRole();
        if (!oldRole.getName().equals(newRole.getName())) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.ROLE_NAME_UPDATE.roleLog(maker, Role.getRole(event.getNewRole()), oldRole.getName(), newRole.getName());
        }
        if (oldRole.isHoisted() != newRole.isHoisted()) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.ROLE_HOIST_UPDATE.roleLog(maker, Role.getRole(event.getNewRole()), String.valueOf(oldRole.isHoisted()), String.valueOf(newRole.isHoisted()));
        }
        if (oldRole.isMentionable() != newRole.isMentionable()) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.ROLE_MENTIONABLE_UPDATE.roleLog(maker, Role.getRole(event.getNewRole()), String.valueOf(oldRole.isMentionable()), String.valueOf(newRole.isMentionable()));
        }
        if (!oldRole.getColor().equals(newRole.getColor())) {
            MessageMaker maker = new MessageMaker(channel);
            maker.withColor(newRole.getColor());
            Logging.ROLE_COLOUR_UPDATE.roleLog(maker, Role.getRole(event.getNewRole()), ("RGB: " + oldRole.getColor().getRed() + ", " + oldRole.getColor().getGreen() + ", " + oldRole.getColor().getBlue()), ("RGB: " + newRole.getColor().getRed() + ", " + newRole.getColor().getGreen() + ", " + newRole.getColor().getBlue()));
        }
        if (oldRole.getPermissions().contains(Permissions.ADMINISTRATOR) != newRole.getPermissions().contains(Permissions.ADMINISTRATOR)) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.ROLE_ADMINISTRATOR_UPDATE.roleLog(maker, Role.getRole(newRole), String.valueOf(oldRole.getPermissions().contains(Permissions.ADMINISTRATOR)), String.valueOf(newRole.getPermissions().contains(Permissions.ADMINISTRATOR)));
        }
    }
    @EventListener
    public void handle(DiscordUserRolesUpdateEvent event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        User user = event.getUser();
        MessageMaker maker = new MessageMaker(channel);
        Collection<Role> oldRoles = event.oldRoles();
        Collection<Role> newRoles = event.newRoles();
        Collection<Role> temp = event.oldRoles();
        oldRoles.removeAll(newRoles);
        newRoles.removeAll(temp);
        if (newRoles.size() == 0) {
            Role removed = ((List<Role>) oldRoles).get(0);
            Logging.USER_ROLE_REMOVED.userRoleLog(maker, removed, user);
        } else if (oldRoles.size() == 0) {
            Role added = ((List<Role>) newRoles).get(0);
            Logging.USER_ROLE_ADDED.userRoleLog(maker, added, user);
        }
    }
}
