package com.github.nija123098.evelyn.discordobjects.wrappers.event.botevents;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * @author nija123098
 * @since 1.0.0
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
