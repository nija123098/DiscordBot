package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.obj.IChannel;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DiscordChannelUpdate implements BotEvent {
    private ChannelUpdateEvent event;
    public DiscordChannelUpdate(ChannelUpdateEvent event) {
        this.event = event;
    }
    public IChannel getOldChannel() {
        return this.event.getOldChannel();
    }
    public IChannel getNewChannel() {
        return this.event.getNewChannel();
    }
    public Channel getChannel() {
        return Channel.getChannel(this.event.getNewChannel());
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
