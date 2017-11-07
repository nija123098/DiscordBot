package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserPardonEvent;

/**
 * Made by nija123098 on 8/7/2017.
 */
public class DiscordUserPardoned implements BotEvent {
    private UserPardonEvent event;
    public DiscordUserPardoned(UserPardonEvent event) {
        this.event = event;
    }
    public User getUser(){
        return User.getUser(this.event.getUser());
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
}
