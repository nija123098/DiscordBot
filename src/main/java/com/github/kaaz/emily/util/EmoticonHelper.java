package com.github.kaaz.emily.util;


import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class EmoticonHelper {
    private static final Map<String, Emoticon> EMOTICON_MAP = new HashMap<>(EmojiManager.getAll().size());
    static {
        EmojiManager.getAll().forEach(emoji -> {
            Emoticon emoticon = new Emoticon(emoji);
            EMOTICON_MAP.put(emoji.getUnicode(), emoticon);
            emoji.getAliases().forEach(s -> EMOTICON_MAP.put(s, emoticon));
        });
    }
    public static String getChars(String name){
        if (EmojiManager.getForAlias(name) == null){
            return null;
        }
        return EmojiManager.getForAlias(name).getUnicode();
    }
    public static String getName(String chars){
        return EmojiManager.getByUnicode(chars).getAliases().get(0);
    }
    public static Emoticon getEmoticon(String in){
        return EMOTICON_MAP.get(in);
    }
    public static class Emoticon {
        private Emoji emoji;
        private Emoticon(Emoji emoji) {
            this.emoji = emoji;
        }
        public Collection<String> names(){
            return this.emoji.getAliases();
        }
        public String chars(){
            return this.emoji.getUnicode();
        }
    }
}
