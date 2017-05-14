package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.MessageUpdateEvent;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class DiscordMessageEditEvent implements BotEvent {
    private MessageUpdateEvent event;
    public DiscordMessageEditEvent(MessageUpdateEvent event) {
        this.event = event;
    }
    public Message getMessage(){
        return Message.getMessage(this.event.getMessage());
    }
    public User getAuthor(){
        return User.getUser(this.event.getAuthor());
    }
    public Channel getChannel(){
        return Channel.getChannel(this.event.getChannel());
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
}
