package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandHandler {
    private static final Map<Guild, Map<String, Template>> NAME_MAP = new ConcurrentHashMap<>();
    private static final Map<Guild, Map<Matcher, Template>> RAGEX_MAP = new ConcurrentHashMap<>();
    public static boolean handle(Guild guild, User user, Shard shard, Channel channel, Message message, String string){
        ensureGuildLoaded(guild);
        Template template = NAME_MAP.get(guild).get(string.split(" ")[0]);
        if (template == null) return false;
        new MessageMaker(message).appendRaw(template.interpret(user, shard, channel, guild, message, null, loadArguments(template.getDefinition().getArgTypes(), user, shard, channel, guild, message, message.getContent()))).send();
        return true;
    }
    public static boolean handleRagex(Guild guild, User user, Shard shard, Channel channel, Message message, String string){
        ensureGuildLoaded(guild);
        AtomicBoolean match = new AtomicBoolean();// ID is interned
        synchronized (guild.getID()){// may want to just queue, but that would require another map
            RAGEX_MAP.get(guild).forEach((matcher, template) -> {
                matcher.reset(string);
                if (matcher.matches()) {
                    template.interpret(user, shard, channel, guild, message, null);
                    match.set(true);
                }
            });
        }
        return match.get();
    }
    private static void ensureGuildLoaded(Guild guild){
        if (!NAME_MAP.containsKey(guild)) loadGuild(guild);
    }
    public static void loadGuild(Guild g){
        List<CustomCommand> commands = ConfigHandler.getSetting(CustomCommandConfig.class, g);
        Map<String, Template> nameMap = new HashMap<>(commands.size());
        Map<Matcher, Template> ragexMap = new HashMap<>(commands.size());
        commands.forEach(c -> {
            nameMap.put(c.getName(), c.getTemplate());
            if (c.getRagex() != null) ragexMap.put(Pattern.compile(c.getRagex()).matcher(""), c.getTemplate());
        });
        NAME_MAP.put(g, nameMap);
        RAGEX_MAP.put(g, ragexMap.isEmpty() ? Collections.emptyMap() : ragexMap);
    }
    public static Set<String> getCustomCommandNames(Guild guild){
        ensureGuildLoaded(guild);
        return NAME_MAP.get(guild).keySet();
    }
    private static Object[] loadArguments(Class<?>[] argTypes, User user, Shard shard, Channel channel, Guild guild, Message message, String s){
        Object[] objects = new Object[argTypes.length];
        for (int i = 0; i < objects.length; i++) {
            Pair<?, Integer> pair = InvocationObjectGetter.convert(argTypes[i], user, shard, channel, guild, message, null, s);
            s = s.substring(pair.getValue());
            objects[i] = pair.getKey();
        }
        return objects;
    }
}
