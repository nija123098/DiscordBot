package com.github.kaaz.emily.util;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Made by nija123098 on 3/24/2017.
 */
public class TypeTranslator {
    private static final Map<Class<?>, Map<Class<?>, Function<?, ?>>> MAP = new HashMap<>();
    public static <I, O> O translate(Class<O> dest, I in){
        if (in == null) {
            return null;
        }
        if (in.getClass().equals(dest)){
            return (O) in;
        }
        Map<Class<?>, Function<?, ?>> map = MAP.get(in.getClass());
        if (map == null){
            throw new RuntimeException("No translation from type: " + in.getClass().getName());
        }
        Function<I, O> function = (Function<I, O>) map.get(dest);// safe
        if (function == null){
            throw new RuntimeException("No translation to type: " + dest.getName());
        }
        return function.apply(in);
    }
    private static <I, O> void add(Class<I> it, Class<O> ot, Function<I, O> function){
        MAP.computeIfAbsent(it, clazz -> new HashMap<>()).put(ot, function);
    }
    static {

    }
}
