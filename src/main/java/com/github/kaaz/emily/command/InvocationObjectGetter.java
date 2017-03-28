package com.github.kaaz.emily.command;

import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 3/27/2017.
 */
public class InvocationObjectGetter {
    private static final Map<Class<?>, Context<?>> CONTEXT_MAP = new HashMap<>();
    private static <T> void add(Class<T> clazz, Context<T> context){
        CONTEXT_MAP.put(clazz, context);
    }
    static {
        add(User.class, (user, message, reaction, args) -> user);
        add(Message.class, (user, message, reaction, args) -> message);
        add(Reaction.class, (user, message, reaction, args) -> reaction);
        add(Guild.class, (user, message, reaction, args) -> message.getGuild());
        add(Channel.class, (user, message, reaction, args) -> message.getChannel());
        add(Presence.class, (user, message, reaction, args) -> user.getPresence());
        add(String[].class, (user, message, reaction, args) -> FormatHelper.reduceRepeats(args, ' ').split(" "));
        add(MessageHelper.class, (user, message, reaction, args) -> new MessageHelper(user, message.getChannel()));
        add(VoiceChannel.class, (user, message, reaction, args) -> message.getGuild().getConnectedVoiceChannel());
        add(Shard.class, (user, message, reaction, args) -> message.getShard());
        add(Region.class, (user, message, reaction, args) -> message.getGuild().getRegion());
        add(Attachment[].class, (user, message, reaction, args) -> {
            List<Attachment> attachments = message.getAttachments();
            return attachments.toArray(new Attachment[attachments.size()]);
        });
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Invocation Object Getter initialized");
    }
    @FunctionalInterface
    private interface Context<E>{
        E getObject(User user, Message message, Reaction reaction, String args);
    }
    public static Object[] replace(Class<?>[] types, Object[] objects, User user, Message message, Reaction reaction, String args){
        for (int i = 0; i < types.length; i++) {
            objects[i] = CONTEXT_MAP.get(types[i]).getObject(user, message, reaction, args);
        }
        return objects;
    }
}
