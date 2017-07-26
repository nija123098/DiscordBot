package com.github.kaaz.emily.util;


import com.github.kaaz.emily.launcher.BotConfig;

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
            Files.readAllLines(Paths.get(BotConfig.CONTAINER_PATH, "Emoticons.txt")).forEach(s -> {
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
    public static String getName(String chars){
        return NAME_MAP.get(chars);
    }
    public static Map<String, Set<String>> getAll(){
        return MAP;
    }
}
