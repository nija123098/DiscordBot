package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/4/2017.
 */
public class Reaction {// should not be saved
    static Reaction getReaction(IReaction iReaction){
        return new Reaction(iReaction);
    }
    static List<Reaction> getReactions(List<IReaction> reactions){
        List<Reaction> reacts = new ArrayList<>(reactions.size());
        reactions.forEach(iMessage -> reacts.add(getReaction(iMessage)));
        return reacts;
    }
    private IReaction reaction;
    private Reaction(IReaction reaction) {
        this.reaction = reaction;
    }

    public IReaction reaction() {
        return this.reaction;
    }

    public int getCount() {
        return reaction.getCount();
    }

    public List<User> getUsers() {
        return User.getUsers(ErrorWrapper.wrap((ErrorWrapper.Request<List<IUser>>) () -> reaction().getUsers()));
    }

    public Shard getShard() {
        return Shard.getShard(reaction().getShard());
    }

    public boolean getUserReacted(User user) {
        return reaction().getUserReacted(user.user());
    }

    public boolean getClientReacted() {
        return reaction().getClientReacted();
    }
}
