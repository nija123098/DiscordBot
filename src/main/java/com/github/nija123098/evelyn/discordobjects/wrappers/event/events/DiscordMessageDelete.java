package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordMessageDelete implements BotEvent {
    private MessageDeleteEvent event;
    public DiscordMessageDelete(MessageDeleteEvent event) {
        this.event = event;
    }
    public Message getMessage() {
        return Message.getMessage(this.event.getMessage());
    }
    public User getAuthor() {
        return User.getUser(this.event.getAuthor());
    }
    public Channel getChannel() {
        return Channel.getChannel(this.event.getChannel());
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
