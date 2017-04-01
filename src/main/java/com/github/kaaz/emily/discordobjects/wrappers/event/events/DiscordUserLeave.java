package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.UserLeaveEvent;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class DiscordUserLeave implements BotEvent {
    private UserLeaveEvent event;
    public DiscordUserLeave(UserLeaveEvent event){
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
    public User getUser(){
        return User.getUser(this.event.getUser());
    }
}
