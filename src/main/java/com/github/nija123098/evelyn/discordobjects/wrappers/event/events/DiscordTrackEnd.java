package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordTrackEnd implements BotEvent {
    private TrackFinishEvent event;
    public DiscordTrackEnd(TrackFinishEvent event) {
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getPlayer().getGuild());
    }
}
