package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordGuildLeave {
    private GuildLeaveEvent event;
    public DiscordGuildLeave(GuildLeaveEvent event) {
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
}
