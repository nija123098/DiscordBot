package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleUpdateEvent;
import sx.blah.discord.handle.obj.IRole;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DiscordRoleUpdate implements BotEvent {
    private RoleUpdateEvent event;
    public DiscordRoleUpdate(RoleUpdateEvent event) {
        this.event = event;
    }
    public IRole getOldRole() {
        return this.event.getOldRole();
    }
    public IRole getNewRole() {
        return this.event.getNewRole();
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
