package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleCreateEvent;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DiscordRoleCreate implements BotEvent {
    private RoleCreateEvent event;
    public DiscordRoleCreate(RoleCreateEvent event) {
        this.event = event;
    }
    public Role getRole() {
        return Role.getRole(this.event.getRole());
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
