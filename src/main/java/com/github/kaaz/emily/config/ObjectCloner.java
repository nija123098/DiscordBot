package com.github.kaaz.emily.config;

import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.ReflectionHelper;
import javafx.util.Pair;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class ObjectCloner {
    private static final Map<Class<?>, Function<Object, Object>> MAP = new HashMap<>();
    static {
        add(Number.class);
        add(Boolean.class);
        add(Class.class);
        add(File.class);
        add(String.class);
        add(Configurable.class);
        add(List.class, CopyOnWriteArrayList::new);
        add(Set.class, or -> {
            Set<?> set = new ConcurrentHashSet();
            set.addAll(or);
            return set;
        });
        add(Map.class, or -> {
            Map<?, ?> map = new ConcurrentHashMap(or.size());
            map.putAll(or);
            return map;
        });
        add(Pair.class, or -> new Pair(or.getKey(), or.getValue()));
    }
    private static <T> void add(Class<T> clazz, Function<T, T> function){
        MAP.put(clazz, (Function<Object, Object>) function);
    }
    private static <T> void add(Class<T> clazz){
        MAP.put(clazz, o -> o);
    }
    public static boolean supports(Class<?> c){
        if (c.isEnum()) return true;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(c)) if (MAP.get(clazz) != null) return true;
        return false;
    }
    public static <I> I clone(I i){
        if (i == null) return null;
        if (i.getClass().isEnum()) return i;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(i.getClass())) {
            Function<Object, Object> function = MAP.get(clazz);
            if (function != null) return (I) function.apply(i);
        }
        throw new ArgumentException("Attempted to clone a object of non-supported type");
    }
}
