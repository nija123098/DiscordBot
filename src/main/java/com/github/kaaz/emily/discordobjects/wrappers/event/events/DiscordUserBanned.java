package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;

/**
 * Made by nija123098 on 8/7/2017.
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