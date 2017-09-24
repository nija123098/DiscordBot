package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;

import java.util.List;

/**
 * Made by nija123098 on 3/13/2017.
 */
public class DiscordUserRolesUpdateEvent implements BotEvent {
    private UserRoleUpdateEvent event;

    public DiscordUserRolesUpdateEvent(UserRoleUpdateEvent event) {
        this.event = event;
    }

    public User getUser(){
        return User.getUser(this.event.getUser());
    }

    public List<Role> oldRoles(){
        return Role.getRoles(this.event.getOldRoles());
    }

    public List<Role> newRoles(){
        return Role.getRoles(this.event.getNewRoles());
    }
}
