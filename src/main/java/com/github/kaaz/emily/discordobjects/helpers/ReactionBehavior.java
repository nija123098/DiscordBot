package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.util.EmoticonHelper;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * Made by nija123098 on 3/26/2017.
 */
@FunctionalInterface
public interface ReactionBehavior {
    void behave(boolean add, Reaction reaction);
    long PERSISTENCE = 120000;
    List<Triple<Message, String, ReactionBehavior>> BEHAVIORS = new MemoryManagementService.ManagedList<>(PERSISTENCE);
    static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        BEHAVIORS.add(new MutableTriple<>(message, emoticonName, behavior));
        message.addReaction(EmoticonHelper.getChars(emoticonName));
    }
    @EventListener
    static void handle(DiscordReactionEvent reaction){
        if (DiscordClient.getOurUser().equals(reaction.getUser())){
            return;
        }
        for (Triple<Message, String, ReactionBehavior> triple : BEHAVIORS){
            if (reaction.getMessage().equals(triple.getLeft()) && triple.getMiddle().equals(reaction.getReaction().getName())){
                triple.getRight().behave(reaction.addingReaction(), reaction.getReaction());
            }
        }
    }
}
