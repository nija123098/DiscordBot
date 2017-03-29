package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class DiscordReactionEvent implements BotEvent {
    private ReactionEvent event;
    public DiscordReactionEvent(ReactionAddEvent event) {
        this.event = event;
    }
    public Reaction getReaction(){
        return Reaction.getReaction(this.event.getReaction());
    }
    public User getUser(){
        return User.getUser(this.event.getUser());
    }
    public Message getMessage(){
        return Message.getMessage(this.event.getMessage());
    }
    public boolean addingReaction(){
        return this.event instanceof ReactionAddEvent;
    }
}
