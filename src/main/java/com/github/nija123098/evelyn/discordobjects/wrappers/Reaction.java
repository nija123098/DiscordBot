package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.discordobjects.exception.ErrorWrapper;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 3/4/2017.
 */
public class Reaction {// should not be saved
    private static final Map<IReaction, Reaction> MAP = new MemoryManagementService.ManagedMap<>(150000);
    public static Reaction getReaction(IReaction iReaction){
        if (iReaction == null) return null;
        return MAP.computeIfAbsent(iReaction, r -> new Reaction(iReaction));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction1 = (Reaction) o;
        return this.reaction.equals(reaction1.reaction);
    }

    @Override
    public int hashCode() {
        return this.reaction.getMessage().hashCode();
    }

    public Message getMessage(){
        return Message.getMessage(this.reaction.getMessage());
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
        return reaction().getUserReacted(DiscordClient.getOurUser().user());
    }

    public String getChars(){
        return reaction().getEmoji().getName();
    }

    public String getName() {
        return EmoticonHelper.getName(reaction().getEmoji().getName());
    }
}
