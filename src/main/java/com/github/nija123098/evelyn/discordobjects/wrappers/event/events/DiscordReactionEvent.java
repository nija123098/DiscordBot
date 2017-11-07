package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class DiscordReactionEvent implements BotEvent {
    private ReactionEvent event;
    public DiscordReactionEvent(ReactionEvent event){
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
