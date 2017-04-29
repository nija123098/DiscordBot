package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class DiscordMessageDeleteEvent implements BotEvent {
    private MessageDeleteEvent event;
    public DiscordMessageDeleteEvent(MessageDeleteEvent event) {
        this.event = event;
    }
    public Message getMessage(){
        return Message.getMessage(this.event.getMessage());
    }
}
