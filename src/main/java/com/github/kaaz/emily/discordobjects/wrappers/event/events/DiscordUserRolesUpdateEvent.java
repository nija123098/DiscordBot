package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.UserRoleUpdateEvent;

import java.util.List;

/**
 * Made by nija123098 on 3/13/2017.
 */
public class DiscordUserRolesUpdateEvent implements BotEvent {
    private UserRoleUpdateEvent event;

    public DiscordUserRolesUpdateEvent(UserRoleUpdateEvent event) {
        this.event = event;
    }

    public List<Role> oldRoles(){
        return Role.getRoles(this.event.getOldRoles());
    }

    public List<Role> newRoles(){
        return Role.getRoles(this.event.getNewRoles());
    }
}
