package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordMessageEditEvent implements BotEvent {
    private MessageEditEvent event;
    public DiscordMessageEditEvent(MessageEditEvent event) {
        this.event = event;
    }
    public Message getOldMessage(){
        return Message.getMessage(this.event.getOldMessage());
    }
    public String getOldMessageCleanedContent(){
        return Message.getCleanContent(this.event.getOldMessage());
    }
    public Message getNewMessage() {
        return Message.getMessage(this.event.getNewMessage());
    }
    public String getNewMessageCleanedString() {
        return Message.getCleanContent(this.event.getNewMessage());
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
