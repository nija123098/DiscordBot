package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Made by nija123098 on 3/27/2017.
 */
public class DiscordMessageReceivedEvent implements BotEvent {
    private MessageReceivedEvent event;
    public DiscordMessageReceivedEvent(MessageReceivedEvent event) {
        this.event = event;
    }
    public Message getMessage(){
        return Message.getMessage(this.event.getMessage());
    }
}
