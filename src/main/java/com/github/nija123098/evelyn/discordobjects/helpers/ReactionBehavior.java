package com.github.nija123098.evelyn.discordobjects.helpers;

import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

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
    long PERSISTENCE = 120000;
    Map<Message, Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>>> BEHAVIORS = new HashMap<>();

    /**
     * Registers a listener to preform a
     *
     * @param message the message to listen to reactions for
     * @param emoticonName the {@link Reaction} to listen for specified by name
     * @param behavior the behavior to preform
     */
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        if (message == null) return;
        message.addReactionByName(emoticonName);
        BEHAVIORS.computeIfAbsent(message, m -> new HashMap<>()).computeIfAbsent(emoticonName, s -> new MutablePair<>(behavior, ScheduleService.schedule(PERSISTENCE, () -> deregisterListener(message, emoticonName))));
    }

    /**
     * De-registers the listener for the {@link Reaction}
     * specified by name for the specified {@link Message}.
     *
     * @param message the message to deregister a reaction for
     * @param emoticonName the {@link Reaction} to deregister a {@link ReactionBehavior} specified by name
     */
    static void deregisterListener(Message message, String emoticonName){
        if (message == null) return;
        Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>> map = BEHAVIORS.get(message);
        if (map == null) return;
        message.removeReactionByName(emoticonName);
        if (map.size() == 1) BEHAVIORS.remove(message);
        else map.remove(emoticonName);
    }

    /**
     * De-registers all {@link ReactionBehavior} listeners.
     */
    static void deregisterAll(){
        Map<Message, Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>>> behaviors = new HashMap<>(BEHAVIORS);
        BEHAVIORS.clear();
        behaviors.forEach((message, map) -> map.keySet().forEach(message::removeReaction));
    }

    /**
     * Listens to {@link Reaction}s to activate the appropriate {@link ReactionBehavior}s for.
     *
     * @param reaction the event to listen to
     */
    @EventListener
    static void handle(DiscordReactionEvent reaction){
        if (reaction.getUser().isBot()) return;
        Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>> map = BEHAVIORS.get(reaction.getMessage());
        if (map == null) return;
        Pair<ReactionBehavior, ScheduleService.ScheduledTask> pair = map.get(reaction.getReaction().getName());
        if (pair == null) return;
        map.forEach((s, par) -> par.setValue(ScheduleService.schedule(PERSISTENCE, () -> deregisterListener(reaction.getMessage(), reaction.getReaction().getName()))).cancel());
        pair.getLeft().behave(reaction.addingReaction(), reaction.getReaction(), reaction.getUser());
    }
}
