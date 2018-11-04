package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.obj.IGuild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordGuildUpdate implements BotEvent {
    private GuildUpdateEvent event;
    public DiscordGuildUpdate(GuildUpdateEvent event) {
        this.event = event;
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
    public IGuild getOldGuild() {
        return this.event.getOldGuild();
    }
    public IGuild getNewGuild() {
        return this.event.getNewGuild();
    }
    public String oldName() {
        return this.event.getOldGuild().getName();
    }
    public String newName() {
        return this.event.getNewGuild().getName();
    }
    public boolean nameChange() {
        return !this.oldName().equals(this.newName());
    }
}
