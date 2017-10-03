package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;
import com.github.nija123098.evelyn.util.ReflectionHelper;
import javafx.util.Pair;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Clones objects mainly for {@link AbstractConfig#alterSetting(Configurable, Consumer)}.
 *
 * @author nija123098
 * @since 1.0.0
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
        add(Set.class, ObjectCloner::getSet);
        add(Map.class, ObjectCloner::getMap);
        add(Pair.class, or -> new Pair(or.getKey(), or.getValue()));
        add(SpecialPermsContainer.class, SpecialPermsContainer::copy);
    }
    private static <T> void add(Class<T> clazz, Function<T, T> function){
        MAP.put(clazz, (Function<Object, Object>) function);
    }
    private static <T> void add(Class<T> clazz){
        MAP.put(clazz, o -> o);
    }

    /**
     * Returns if the {@link ObjectCloner} supports cloning the class type
     *
     * @param c the class type for checking if cloning is supported
     * @return if the {@link ObjectCloner} supports cloning the class type
     */
    static boolean supports(Class<?> c){
        if (c.isEnum()) return true;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(c)) if (MAP.get(clazz) != null) return true;
        return false;
    }

    /**
     * Clones an object.
     *
     * @param i the object to clone
     * @param <I> the type of the cloned object
     * @return a cloned object
     */
    static <I> I clone(I i){
        if (i == null) return null;
        if (i.getClass().isEnum()) return i;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(i.getClass())) {
            Function<Object, Object> function = MAP.get(clazz);
            if (function != null) return (I) function.apply(i);
        }
        throw new DevelopmentException("Attempted to clone a object of non-supported type");
    }
    private static <E> Set<E> getSet(Set<E> es){
        Set<E> objects = new ConcurrentHashSet<>();
        objects.addAll(es);
        return objects;
    }
    private static <E, F> Map<E, F> getMap(Map<E, F> es){
        Map<E, F> objects = new ConcurrentHashMap<>();
        objects.putAll(es);
        return objects;
    }
}
