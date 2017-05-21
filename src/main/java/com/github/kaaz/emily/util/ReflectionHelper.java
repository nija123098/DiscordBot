package com.github.kaaz.emily.util;

import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class ReflectionHelper {
    private static final Set<Class<?>> OBJECT = Collections.singleton(Object.class);
    private static final Map<Class<?>, Set<Class<?>>> SUPERCLASS_MAP = Collections.synchronizedMap(new HashMap<Class<?>, Set<Class<?>>>());
    public static Set<Class<?>> getAssignableTypes(Class<?> lowest) {
        if (lowest == null) return Collections.EMPTY_SET;
        if (lowest == Object.class) return OBJECT;
        return SUPERCLASS_MAP.computeIfAbsent(lowest, l -> {
            Set<Class<?>> classes = new ConcurrentHashSet<>();
            Class<?> clazz = lowest.getSuperclass();
            if (clazz != null) classes.add(clazz);// can not remove null
            Collections.addAll(classes, lowest.getInterfaces());
            classes.forEach(aClass -> classes.addAll(getAssignableTypes(aClass)));
            classes.add(lowest);
            return classes;
        });
    }
}
