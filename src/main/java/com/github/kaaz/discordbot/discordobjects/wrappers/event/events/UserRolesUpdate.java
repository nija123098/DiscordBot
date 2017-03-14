package com.github.kaaz.discordbot.discordobjects.wrappers.event.events;

import com.github.kaaz.discordbot.discordobjects.wrappers.Role;
import com.github.kaaz.discordbot.discordobjects.wrappers.event.DiscordEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;

import java.util.List;

/**
 * Made by nija123098 on 3/13/2017.
 */
public class UserRolesUpdate implements DiscordEvent {
    private UserRoleUpdateEvent event;

    public UserRolesUpdate(UserRoleUpdateEvent event) {
        this.event = event;
    }

    public List<Role> oldRoles(){
        return Role.getRoles(event.getOldRoles());
    }

    public List<Role> newRoles(){
        return Role.getRoles(event.getNewRoles());
    }
}
