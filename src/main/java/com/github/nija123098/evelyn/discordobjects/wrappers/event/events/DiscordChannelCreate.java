package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DiscordChannelCreate implements BotEvent {
    private ChannelCreateEvent event;
    public DiscordChannelCreate(ChannelCreateEvent event) {
        this.event = event;
    }
    public Channel getChannel() {
        return Channel.getChannel(this.event.getChannel());
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
