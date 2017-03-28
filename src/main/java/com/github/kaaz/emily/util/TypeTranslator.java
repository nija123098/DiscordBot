package com.github.kaaz.emily.util;

import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/24/2017.
 */
public class TypeTranslator {
    private static final Map<Class<?>, Map<Class<?>, Translation<?, ?>>> TRANSLATION_MAP = new HashMap<>();
    public static <I, O> O translate(I in, Class<O> type){
        if (in == null){
            return null;
        }
        return translate(in, type, null);
    }
    public static <I, O> O translate(I in, O context){// sets only support string sets and maps
        if (in == null){
            return null;
        }
        return translate(in, (Class<O>) context.getClass(), context);
    }
    private static <I, O> O translate(I in, Class<O> destinationType, O context){
        final Class<I> originType = (Class<I>) in.getClass();
        if (originType.equals(destinationType)){
            return (O) in;
        }
        final AtomicReference<Map<Class<?>, Translation<?, ?>>> innerMap = new AtomicReference<>();
        TRANSLATION_MAP.forEach((clazz, interiorMap) -> {
            if (innerMap.get() == null && clazz.isAssignableFrom(originType)){
                innerMap.set(interiorMap);
            }
        });
        if (innerMap.get() == null){
            throw new RuntimeException("No translation from type: " + in.getClass().getName());
        }
        final AtomicBoolean set = new AtomicBoolean();
        final AtomicReference<O> value = new AtomicReference<>();
        innerMap.get().forEach((clazz, translation) -> {
            if (!set.get() && clazz.isAssignableFrom(destinationType)){
                set.set(true);
                value.set(((Translation<I, O>) translation).translate(in, context));
            }
        });
        if (!set.get()){
            throw new RuntimeException("No translation to type: " + destinationType.getName());
        }
        return value.get();
    }
    private static <I, O> void add(Class<I> inType, Class<O> outType, Translation<I, O> in, Translation<O, I> out){
        TRANSLATION_MAP.computeIfAbsent(inType, clazz -> new HashMap<>()).put(outType, in);
        TRANSLATION_MAP.computeIfAbsent(outType, clazz -> new HashMap<>()).put(inType, out);
    }
    static {
        add(String.class, Boolean.class, (s, b) -> s.equals("!") ? !b : s.equals("true") || s.equals("yes") || s.equals("1"), (bool, s) -> bool ? "true" : "false");
        add(Set.class, String.class, (set, s) -> {
            final StringBuilder builder = new StringBuilder("");
            set.forEach(o -> builder.append(o).append(", "));
            return builder.toString();
        }, (s, c) -> {
            String[] parts = s.split(" ");
            String[] strings = parts[1].split(", ");
            switch (parts[0]){
                case "set":
                    Set<String> setSet = new ConcurrentHashSet<>();
                    Collections.addAll(setSet, strings);
                    return setSet;
                case "add":
                    Set<String> setAdd = new ConcurrentHashSet<>();
                    setAdd.addAll(c);
                    Collections.addAll(setAdd, strings);
                    return setAdd;
                case "remove":
                    Set<String> setRemove = new ConcurrentHashSet<>();
                    setRemove.addAll(c);
                    for (String string : strings){
                        setRemove.remove(string);
                    }
                    return setRemove;
            }
            throw new UnsupportedOperationException("No operation: " + strings[0]);
        });
    }
    @FunctionalInterface
    private interface Translation<I, O>{
        O translate(I value, O context);
    }
}
