package com.github.nija123098.evelyn.discordobjects.helpers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.nija123098.evelyn.util.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class for preforming functions when specified reactions are called.
 *
 * @author nija13098
 * @since 1.0.0
 * @see MessageMaker
 */
@FunctionalInterface
public interface ReactionBehavior {
    void behave(boolean add, Reaction reaction, User user);
    Cache<Message, Map<String, ReactionBehavior>> CACHE = new Cache<>(ConfigProvider.CACHE_SETTINGS.reactionBehaviorSize(), 120_000, message -> new ConcurrentHashMap<>(), (message, stringReactionBehaviorMap) -> stringReactionBehaviorMap.forEach((s, reactionBehavior) -> message.removeReactionByName(s)));

    /**
     * Registers a listener to preform a behavior
     * when an {@link User} reacts with a registered
     * reaction on a registered {@link Message}.
     *
     * @param message the message to listen to reactions for.
     * @param emoticonName the {@link Reaction} to listen for specified by name.
     * @param behavior the behavior to preform.
     */
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior) {
        if (message == null) return;
        message.addReactionByName(emoticonName);
        CACHE.get(message).putIfAbsent(emoticonName, behavior);
    }

    /**
     * De-registers the listener for the {@link Reaction}
     * specified by name for the specified {@link Message}.
     *
     * @param message the message to deregister a reaction for.
     * @param emoticonName the {@link Reaction} to deregister a {@link ReactionBehavior} specified by name.
     */
    static void deregisterListener(Message message, String emoticonName) {
        if (message == null) return;
        Map<String, ReactionBehavior> map = CACHE.getIfPresent(message);
        if (map == null) return;
        message.removeReactionByName(emoticonName);
        if (map.size() == 1) CACHE.remove(message);
        else map.remove(emoticonName);
    }

    /**
     * De-registers all listeners for the specified {@link Message}.
     *
     * @param message the message to deregister a reaction for.
     */
    static void deregisterListeners(Message message) {
        if (message == null) return;
        Map<String, ReactionBehavior> map = CACHE.getIfPresent(message);
        if (map == null) return;
        CACHE.remove(message);
        map.forEach((s, reactionBehavior) -> message.removeReactionByName(s));
    }

    /**
     * De-registers all {@link ReactionBehavior} listeners.
     */
    static void deregisterAll() {
        CACHE.clear();// ejection does the deciphering
    }

    /**
     * Listens to {@link Reaction}s to activate the appropriate {@link ReactionBehavior}s for.
     *
     * @param reaction the event to listen to.
     */
    @EventListener
    static void handle(DiscordReactionEvent reaction) {
        if (reaction.getUser() == null || !reaction.getMessage().getAuthor().equals(DiscordClient.getOurUser()) || (reaction.getUser().isBot() && reaction.getUser().getLongID() != ConfigProvider.BOT_SETTINGS.managementBot())) return;
        Map<String, ReactionBehavior> map = CACHE.getIfPresent(reaction.getMessage());
        if (map == null) return;
        ReactionBehavior behavior = map.get(reaction.getReaction().getName());
        if (behavior == null) return;
        behavior.behave(reaction.addingReaction(), reaction.getReaction(), reaction.getUser());
    }
}
