package com.github.nija123098.evelyn.discordobjects.helpers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.nija123098.evelyn.util.CacheHelper;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
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
    LoadingCache<Message, Map<String, ReactionBehavior>> CACHE = CacheHelper.getLoadingCache(Runtime.getRuntime().availableProcessors() * 2, ConfigProvider.CACHE_SETTINGS.reactionBehaviorSize(), 120_000, k -> new ConcurrentHashMap<>());

    /**
     * Registers a listener to preform a
     *
     * @param message the message to listen to reactions for.
     * @param emoticonName the {@link Reaction} to listen for specified by name.
     * @param behavior the behavior to preform.
     */
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        if (message == null) return;
        message.addReactionByName(emoticonName);
        if (CACHE.getIfPresent(message) == null) CACHE.put(message, new HashMap<>());
        CACHE.getUnchecked(message).putIfAbsent(emoticonName, behavior);
    }

    /**
     * De-registers the listener for the {@link Reaction}
     * specified by name for the specified {@link Message}.
     *
     * @param message the message to deregister a reaction for.
     * @param emoticonName the {@link Reaction} to deregister a {@link ReactionBehavior} specified by name.
     */
    static void deregisterListener(Message message, String emoticonName){
        if (message == null) return;
        Map<String, ReactionBehavior> map = CACHE.getIfPresent(message);
        if (map == null) return;
        message.removeReactionByName(emoticonName);
        if (map.size() == 1) CACHE.invalidate(message);
        else map.remove(emoticonName);
    }

    /**
     * De-registers all {@link ReactionBehavior} listeners.
     */
    static void deregisterAll(){
        Map<Message, Map<String, ReactionBehavior>> behaviors = new HashMap<>(CACHE.asMap());
        CACHE.invalidateAll();
        behaviors.forEach((message, map) -> map.keySet().forEach(message::removeReaction));
    }

    /**
     * Listens to {@link Reaction}s to activate the appropriate {@link ReactionBehavior}s for.
     *
     * @param reaction the event to listen to.
     */
    @EventListener
    static void handle(DiscordReactionEvent reaction){
        if (reaction.getUser() == null || reaction.getUser().isBot() || !reaction.getMessage().getAuthor().equals(DiscordClient.getOurUser())) return;
        Map<String, ReactionBehavior> map = CACHE.getUnchecked(reaction.getMessage());
        if (map == null) return;
        ReactionBehavior behavior = map.get(reaction.getReaction().getName());
        if (behavior == null) return;
        behavior.behave(reaction.addingReaction(), reaction.getReaction(), reaction.getUser());
    }
}
