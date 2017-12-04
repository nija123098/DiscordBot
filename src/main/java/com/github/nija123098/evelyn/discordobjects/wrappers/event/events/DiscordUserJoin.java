package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;

import java.time.ZoneOffset;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordUserJoin implements BotEvent {
    private UserJoinEvent event;
    public DiscordUserJoin(UserJoinEvent event) {
        this.event = event;
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
    public User getUser() {
        return User.getUser(this.event.getUser());
    }
    public long getJoinTime(){
        return this.event.getJoinTime().toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
