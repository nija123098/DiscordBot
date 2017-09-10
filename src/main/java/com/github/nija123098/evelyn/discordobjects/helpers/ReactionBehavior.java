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
 * Made by nija123098 on 3/26/2017.
 */
@FunctionalInterface
public interface ReactionBehavior {
    void behave(boolean add, Reaction reaction, User user);
    long PERSISTENCE = 120000;
    Map<Message, Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>>> BEHAVIORS = new HashMap<>();
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        if (message == null) return;
        message.addReactionByName(emoticonName);
        BEHAVIORS.computeIfAbsent(message, m -> new HashMap<>()).computeIfAbsent(emoticonName, s -> new MutablePair<>(behavior, ScheduleService.schedule(PERSISTENCE, () -> deregisterListener(message, emoticonName))));
    }
    static void deregisterListener(Message message, String emoticonName){
        if (message == null) return;
        Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>> map = BEHAVIORS.get(message);
        if (map == null) return;
        message.removeReactionByName(emoticonName);
        if (map.size() == 1) BEHAVIORS.remove(message);
        else map.remove(emoticonName);
    }
    static void deregisterAll(){
        Map<Message, Map<String, Pair<ReactionBehavior, ScheduleService.ScheduledTask>>> behaviors = new HashMap<>(BEHAVIORS);
        BEHAVIORS.clear();
        behaviors.forEach((message, map) -> map.keySet().forEach(message::removeReaction));
    }
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
