package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.service.services.ScheduleService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/26/2017.
 */
@FunctionalInterface
public interface ReactionBehavior {
    void behave(boolean add, Reaction reaction, User user);
    long PERSISTENCE = 120000;// map entries needn't be cleared, not many reactions are used so it is not worth synchronizing
    Map<String, Map<Message, Pair<ReactionBehavior, ScheduleService.ScheduledTask>>> BEHAVIORS = new ConcurrentHashMap<>();
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        if (message == null) return;
        BEHAVIORS.computeIfAbsent(emoticonName, n -> new ConcurrentHashMap<>()).put(message, new MutablePair<>(behavior, ScheduleService.schedule(PERSISTENCE, () -> deregisterListener(message, emoticonName))));
        message.addReactionByName(emoticonName);
    }
    static void deregisterListener(Message message, String emoticonName){
        if (message == null) return;
        BEHAVIORS.computeIfAbsent(emoticonName, s -> new ConcurrentHashMap<>()).remove(message);
        message.removeReactionByName(emoticonName);
    }
    @EventListener
    static void handle(DiscordReactionEvent reaction){
        if (DiscordClient.getOurUser().equals(reaction.getUser())) return;
        Map<Message, Pair<ReactionBehavior, ScheduleService.ScheduledTask>> map = BEHAVIORS.get(reaction.getReaction().getName());
        if (map == null) return;
        Pair<ReactionBehavior, ScheduleService.ScheduledTask> pair = map.get(reaction.getMessage());
        if (pair != null) {
            pair.getKey().behave(reaction.addingReaction(), reaction.getReaction(), reaction.getUser());
            pair.getValue().cancel();
            pair.setValue(ScheduleService.schedule(PERSISTENCE, () -> deregisterListener(reaction.getMessage(), reaction.getReaction().getName())));
        }
    }
}
