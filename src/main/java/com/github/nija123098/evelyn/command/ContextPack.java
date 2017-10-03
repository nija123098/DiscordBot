package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.ContextException;
import com.github.nija123098.evelyn.template.commands.functions.ContextCommand;

/**
 * Context Command must be updated if this type's getters are changed.
 *
 * @author nija123098
 * @since 1.0.0
 * @see ContextCommand
 */
public class ContextPack {
    private User user;
    private Shard shard;
    private Channel channel;
    private Guild guild;
    private Message message;
    private Reaction reaction;
    private String args;

    /**
     * A constructor for every possible source of inferring a command's meaning
     *
     * @param user the invoking user
     * @param shard the shard the invocation occurred on
     * @param channel the channel a invocation occurred in
     * @param guild the guild a command occurred in or null
     * @param message the message the command occurred by or a message a reaction command was invoked on or null
     * @param reaction the reaction the command occured by or null
     * @param args the arguments for a command
     */
    public ContextPack(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args) {
        this.user = user;
        this.shard = shard;
        this.channel = channel;
        this.guild = guild;
        this.message = message;
        this.reaction = reaction;
        this.args = args;
    }
    public User getUser() {
        return this.user;
    }
    public Shard getShard() {
        return this.shard;
    }
    public Channel getChannel() {
        return this.channel;
    }
    public Guild getGuild() {
        return this.guild;
    }
    public Message getMessage() {
        return this.message;
    }
    public Reaction getReaction() {
        return this.reaction;
    }
    public String getArgs(){
        return this.args;
    }
    public GuildUser getGuildUser(){
        if (this.guild == null) throw new ContextException("A guild is not present in this context");
        return GuildUser.getGuildUser(this.guild, this.user);
    }
}
