package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordUserBanned implements BotEvent {
    private UserBanEvent event;
    public DiscordUserBanned(UserBanEvent event) {
        this.event = event;
    }
    public User getUser(){
        return User.getUser(this.event.getUser());
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
}
