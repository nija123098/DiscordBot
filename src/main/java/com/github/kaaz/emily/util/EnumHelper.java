package com.github.kaaz.emily.util;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Made by nija123098 on 4/16/2017.
 */
public class EnumHelper {
    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> getSet(Class<E> clazz, E...es){
        switch (es.length){
            case 0:
                return EnumSet.noneOf(clazz);
            case 1:
                return EnumSet.of(es[0]);
        }
        return EnumSet.of(es[0], Arrays.copyOfRange(es, 1, es.length));
    }
}
