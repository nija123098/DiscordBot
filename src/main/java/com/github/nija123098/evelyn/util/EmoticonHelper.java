package com.github.nija123098.evelyn.util;


import com.github.nija123098.evelyn.BotConfig.ReadConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IEmoji;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class EmoticonHelper {
    private static final Map<String, String> EMOTICON_MAP = new HashMap<>();
    private static final Map<String, String> NAME_MAP = new HashMap<>();
    private static final Map<String, Set<String>> MAP = new HashMap<>();
    static {
        try{
            Files.readAllLines(Paths.get(ReadConfig.EMOTICONS_NAME)).forEach(s -> {
                String[] strings = s.split(" ");
                MAP.put(strings[0], new HashSet<>(Arrays.asList(Arrays.copyOfRange(strings, 1, strings.length))));
                for (int i = 1; i < strings.length; i++) EMOTICON_MAP.put(strings[i], strings[0]);
                NAME_MAP.put(strings[0], strings[1]);
            });
        } catch (IOException e) {
            Log.log("Could not load emoticons", e);
        }
    }
    public static String getChars(String name, boolean noSpace){
        String chars = EMOTICON_MAP.get(name);
        if (chars == null) return null;
        if (noSpace) chars += '\u200B';
        return chars;
    }
    public static IEmoji getEmoji(String s) {
        return DiscordClient.getSupportServer().guild().getEmojiByName(s);
    }

    public static ReactionEmoji getReactionEmoji(String s){// re-wrap once D4J v3 releases
        String chars = getChars(s, false);
        if (chars != null) return ReactionEmoji.of(chars);
        IEmoji iEmoji = DiscordClient.getSupportServer().guild().getEmojiByName(s);
        if (iEmoji != null) return ReactionEmoji.of(iEmoji);
        return null;
    }
    public static String getName(String chars){
        return NAME_MAP.get(chars);
    }
    public static Map<String, Set<String>> getAll(){
        return MAP;
    }
}
