package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.MessageDeleteEvent;

/**
 * Made by nija123098 on 7/18/2017.
 */
public class DiscordMessageDelete implements BotEvent {
    private MessageDeleteEvent event;
    public DiscordMessageDelete(MessageDeleteEvent event) {
        this.event = event;
    }
    public User getAuther(){
        return User.getUser(this.event.getAuthor());
    }
    public Channel getChannel(){
        return Channel.getChannel(this.event.getChannel());
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
    public Message getMessage(){
        return Message.getMessage(this.event.getMessage());
    }
}
