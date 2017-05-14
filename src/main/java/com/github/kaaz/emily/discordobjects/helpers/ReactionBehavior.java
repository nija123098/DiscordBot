package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.EmoticonHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/26/2017.
 */
@FunctionalInterface
public interface ReactionBehavior {
    void behave(boolean add, Reaction reaction);
    long PERSISTENCE = 120000;// map entries needn't be cleared, not many reactions are used so it is not worth synchronizing
    Map<String, Map<Message, ReactionBehavior>> BEHAVIORS = new ConcurrentHashMap<>();
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        BEHAVIORS.computeIfAbsent(emoticonName, n -> new ConcurrentHashMap<>()).put(message, behavior);
        message.addReaction(EmoticonHelper.getChars(emoticonName));
        ScheduleService.schedule(PERSISTENCE + System.currentTimeMillis(), () -> unregisteredListener(message, emoticonName));
    }
    static void unregisteredListener(Message message, String emoticonName){
        BEHAVIORS.computeIfAbsent(emoticonName, s -> new ConcurrentHashMap<>()).remove(message);
        message.removeReactionByName(emoticonName);
    }
    @EventListener
    static void handle(DiscordReactionEvent reaction){
        if (DiscordClient.getOurUser().equals(reaction.getUser())) return;
        Map<Message, ReactionBehavior> map = BEHAVIORS.get(reaction.getReaction().getName());
        if (map == null) return;
        ReactionBehavior behavior = map.get(reaction.getMessage());
        if (behavior != null) behavior.behave(reaction.addingReaction(), reaction.getReaction());
    }
}
