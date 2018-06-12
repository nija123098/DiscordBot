package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.util.Cache;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wraps a Discord4j {@link IReaction} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Reaction {// should not be saved
    private static final Map<IReaction, Reaction> CACHE = new Cache<>(ConfigProvider.CACHE_SETTINGS.reactionSize(), 30_000, Reaction::new);
    public static Reaction getReaction(IReaction iReaction) {
        if (iReaction == null) return null;
        return CACHE.get(iReaction);
    }
    static List<Reaction> getReactions(List<IReaction> reactions) {
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
        return reaction().hashCode();
    }

    public Message getMessage() {
        return Message.getMessage(this.reaction.getMessage());
    }

    public int getCount() {
        return reaction().getCount();
    }

    public List<User> getUsers() {
        return User.getUsers(ExceptionWrapper.wrap((ExceptionWrapper.Request<List<IUser>>) () -> reaction().getUsers()));
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

    public String getChars() {
        return reaction().getEmoji().getName();
    }

    public String getName() {
        String name = EmoticonHelper.getName(reaction().getEmoji().getName());
        return name == null ? reaction().getEmoji().getName() : name;
    }
}
