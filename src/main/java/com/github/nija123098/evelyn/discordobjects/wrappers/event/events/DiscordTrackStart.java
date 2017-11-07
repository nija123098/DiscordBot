package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.util.audio.events.TrackStartEvent;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class DiscordTrackStart implements BotEvent {
    private TrackStartEvent event;
    public DiscordTrackStart(TrackStartEvent event) {
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getPlayer().getGuild());
    }
}
