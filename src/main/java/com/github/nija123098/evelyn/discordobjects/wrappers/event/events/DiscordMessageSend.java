package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageSendEvent;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class DiscordMessageSend implements BotEvent {
    private MessageSendEvent event;
    public DiscordMessageSend(MessageSendEvent event) {
        this.event = event;
    }
    public User getUser(){// should always return Emily
        return User.getUser(this.event.getAuthor());
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
    public Channel getChannel(){
        return Channel.getChannel(this.event.getChannel());
    }
    public Message getMessage(){
        return Message.getMessage(this.event.getMessage());
    }
}
