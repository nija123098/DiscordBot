package com.github.kaaz.discordbot.template;

import com.github.kaaz.discordbot.discordwrapperobjects.Channel;
import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.Shard;
import com.github.kaaz.discordbot.discordwrapperobjects.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 2/27/2017.
 */
public class TemplateHandler {
    private static final char leftBrace = '<', rightBrace = '>';
    private static final Map<String, TemplateFunction> REPLACEMENT_FUNCTIONS;
    static {
        REPLACEMENT_FUNCTIONS = new HashMap<>();
        // addFunction calls here
    }
    private static void addFunction(String name, TemplateFunction function){
        REPLACEMENT_FUNCTIONS.put(name, function);
    }
    public static String interpret(String in, User caller, Channel channel, Guild guild, Shard shard){
        StringBuilder builder = new StringBuilder();
        int ind = in.indexOf(rightBrace);
        if (ind == -1){
            return in;
        }
        boolean foundLeft = false;
        for (int i = ind; i > -1 && (in.charAt(i) != ' ' || !foundLeft) && !(in.charAt(i) == leftBrace && foundLeft); --i) {
            if (in.charAt(i) == leftBrace){
                foundLeft = true;
            }
            builder.append(in.charAt(i));
        }
        String rep = builder.reverse().toString();
        String[] repS = rep.split(leftBrace + "");
        repS[1] = repS[1].substring(0, repS[1].length() - 1);
        return interpret(in.replaceFirst(rep, REPLACEMENT_FUNCTIONS.get(repS[0]).apply(repS[1], caller, channel, guild, shard)), caller, channel, guild, shard);
    }
    @FunctionalInterface
    public static interface TemplateFunction {
        String apply(String in, User caller, Channel channel, Guild guild, Shard shard);
    }
}
