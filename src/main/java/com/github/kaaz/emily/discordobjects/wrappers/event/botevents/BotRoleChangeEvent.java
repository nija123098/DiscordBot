package com.github.kaaz.emily.discordobjects.wrappers.event.botevents;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/14/2017.
 */
public class BotRoleChangeEvent implements BotEvent {
    private boolean grant;
    private BotRole role;
    private User user;
    private Guild guild;
    public BotRoleChangeEvent(boolean grant, BotRole role, User user, Guild guild) {
        this.grant = grant;
        this.role = role;
        this.user = user;
        this.guild = guild;
    }
    public boolean isGrant() {
        return this.grant;
    }

    public BotRole getRole() {
        return this.role;
    }

    public User getUser() {
        return this.user;
    }

    public Guild getGuild() {
        return this.guild;
    }
}
