package com.github.nija123098.evelyn.util;


import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IEmoji;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.RESOURCE_FILES;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient.getSupportServer;
import static com.github.nija123098.evelyn.util.Log.log;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import static sx.blah.discord.handle.impl.obj.ReactionEmoji.of;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class EmoticonHelper {
    private static final Map<String, String> EMOTICON_MAP = new HashMap<>();
    private static final Map<String, String> EMOTICON_UNICODE_MAP = new HashMap<>();
    private static final Map<String, String> NAME_MAP = new HashMap<>();
    private static final Map<String, Set<String>> MAP = new HashMap<>();

    static {
        try {
            readAllLines(get(RESOURCE_FILES.emoticons())).forEach(s -> {
                String[] strings = s.split(" ");
                MAP.put(strings[0], new HashSet<>(asList(copyOfRange(strings, 1, strings.length))));
                for (int i = 1; i < strings.length; i++) EMOTICON_MAP.put(strings[i], strings[0]);
                for (int i = 0; i < strings.length; i++) EMOTICON_UNICODE_MAP.put(strings[0], strings[i]);
                NAME_MAP.put(strings[0], strings[1]);
            });
        } catch (IOException e) {
            log("Could not load emoticons", e);
        }
    }

    public static String getChars(String name, boolean noSpace) {
        String chars = EMOTICON_MAP.get(name);
        if (chars == null) return null;
        if (noSpace) chars += '\u200B';
        return chars;
    }

    public static boolean checkUnicode(String name) {
        String chars = EMOTICON_UNICODE_MAP.get(name);
        try {
            return chars.equals(name);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static IEmoji getEmoji(String s) {
        return getSupportServer().guild().getEmojiByName(s);
    }

    public static ReactionEmoji getReactionEmoji(String s) {// re-wrap once D4J v3 releases
        String chars = getChars(s, false);
        if (chars != null) return of(chars);
        IEmoji iEmoji = getSupportServer().guild().getEmojiByName(s);
        if (iEmoji != null) return of(iEmoji);
        return null;
    }

    public static String getName(String chars) {
        return NAME_MAP.get(chars);
    }

    public static Map<String, Set<String>> getAll() {
        return MAP;
    }
}
