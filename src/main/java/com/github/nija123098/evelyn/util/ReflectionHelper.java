package com.github.nija123098.evelyn.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ReflectionHelper {
    private static final Set<Class<?>> OBJECT = singleton(Object.class);
    private static final Map<Class<?>, Set<Class<?>>> SUPERCLASS_MAP = new HashMap<>();

    public static Set<Class<?>> getAssignableTypes(Class<?> lowest) {
        if (lowest == null) return emptySet();
        if (lowest == Object.class) return OBJECT;
        return SUPERCLASS_MAP.computeIfAbsent(lowest, l -> {
            Set<Class<?>> classes = ConcurrentHashMap.newKeySet();
            Class<?> clazz = lowest.getSuperclass();
            if (clazz != null) classes.add(clazz);// can not remove null
            addAll(classes, lowest.getInterfaces());
            classes.forEach(aClass -> classes.addAll(getAssignableTypes(aClass)));
            classes.add(lowest);
            return new HashSet<>(classes);
        });
    }
}
