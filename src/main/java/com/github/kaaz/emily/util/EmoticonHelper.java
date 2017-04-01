package com.github.kaaz.emily.util;


import com.vdurmont.emoji.EmojiManager;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class EmoticonHelper {
    public static String getChars(String name){
        return EmojiManager.getForAlias(name).getUnicode();
    }
    public static String getName(String chars){
        return EmojiManager.getByUnicode(chars).getAliases().get(0);
    }
}
