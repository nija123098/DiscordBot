package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.GuildUpdateEvent;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class DiscordGuildUpdate implements BotEvent {
    private GuildUpdateEvent event;
    public DiscordGuildUpdate(GuildUpdateEvent event) {
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
    public String oldName(){
        return this.event.getOldGuild().getName();
    }
    public String newName(){
        return this.event.getNewGuild().getName();
    }
    public boolean nameChange(){
        return !this.oldName().equals(this.newName());
    }
}
