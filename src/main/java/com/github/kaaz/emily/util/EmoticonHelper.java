package com.github.kaaz.emily.util;


import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class EmoticonHelper {
    private static final BidiMap<String, String> MAP = new DualHashBidiMap<>();// todo
    public static String getChars(String name){
        return MAP.get(name);
    }
    public static String getName(String chars){
        return MAP.getKey(chars);
    }
}
