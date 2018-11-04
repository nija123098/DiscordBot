package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleDeleteEvent;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DiscordRoleDelete implements BotEvent {
    private RoleDeleteEvent event;
    public DiscordRoleDelete(RoleDeleteEvent event) {
        this.event = event;
    }
    public Role getRole() {
        return Role.getRole(this.event.getRole());
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
