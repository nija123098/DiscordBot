package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReaction;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.util.Quadruple;

import java.util.ArrayList;
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
    private static final List<Quadruple<Long, Message, String, ReactionBehavior>> QUADRUPLES = new ArrayList<>();
    public static void registerListener(Message message, String emoticonName, ReactionBehavior behavior){
        QUADRUPLES.add(new Quadruple<>(System.currentTimeMillis() + PERSISTENCE, message, emoticonName, behavior));
    }
    public static void handle(DiscordReaction reaction){
        for (Quadruple<Long, Message, String, ReactionBehavior> quad : QUADRUPLES){
            if (reaction.getMessage().equals(quad.getB()) && quad.getC().equals(reaction.getReaction().getName())){
                quad.getD().behave(reaction.addingReaction(), reaction.getReaction());
            }
        }
    }
    static {
        MemoryManagementService.register(ReactionBehaviorHandler::clean);
    }
    private static void clean(){
        long current = System.currentTimeMillis();
        while (true){
            if (QUADRUPLES.get(0).a >= current){
                QUADRUPLES.remove(0);
            }else{
                return;
            }
        }
    }
}
