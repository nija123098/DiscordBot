package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReaction;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class ReactionBehaviorHandler {
    @FunctionalInterface
    public interface ReactionBehavior{
        void behave(boolean add, Reaction reaction);
    }
    private static final long PERSISTENCE = 120000;
    private static final List<Triple<Message, String, ReactionBehavior>> QUADRUPLES = new MemoryManagementService.ManagedList<>(PERSISTENCE);
    public static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        QUADRUPLES.add(new MutableTriple<>(message, emoticonName, behavior));
    }
    public static void handle(DiscordReaction reaction){
        for (Triple<Message, String, ReactionBehavior> quad : QUADRUPLES){
            if (reaction.getMessage().equals(quad.getLeft()) && quad.getMiddle().equals(reaction.getReaction().getName())){
                quad.getRight().behave(reaction.addingReaction(), reaction.getReaction());
            }
        }
    }
}
