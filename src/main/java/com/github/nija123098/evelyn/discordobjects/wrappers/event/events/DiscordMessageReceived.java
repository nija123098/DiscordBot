package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordMessageReceived implements BotEvent {
    private boolean command;
    private MessageReceivedEvent event;
    public DiscordMessageReceived(MessageReceivedEvent event) {
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
    public void setCommand(boolean command) {
        this.command = command;
    }
    public boolean isCommand() {
        return this.command;
    }
}
