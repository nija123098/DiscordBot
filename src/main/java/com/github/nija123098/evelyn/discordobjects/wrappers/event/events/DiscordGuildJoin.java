package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordGuildJoin implements BotEvent {
    private GuildCreateEvent event;
    public DiscordGuildJoin(GuildCreateEvent event) {
        this.event = event;
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
