package com.github.kaaz.emily.discordobjects.wrappers.event;

import com.github.kaaz.emily.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 3/12/2017.
 */
public class EventDistributor {
    private static final Map<Class<?>, Set<Listener>> LISTENER_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<Class<?>>> SUPERCLASS_MAP = new ConcurrentHashMap<>();
    public static <E extends BotEvent> void register(Object o){
        Class<?> clazz;
        if (o instanceof Class){
            clazz = (Class<?>) o;
        } else {
            clazz = o.getClass();
        }
        Stream.of(clazz.getMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).filter(method -> method.getParameterCount() == 1).filter(method -> BotEvent.class.isAssignableFrom(method.getParameterTypes()[0])).forEach(method -> {
            Class<E> peram = (Class<E>) method.getParameterTypes()[0];
            LISTENER_MAP.computeIfAbsent(peram, cl -> new HashSet<>());
            if (Modifier.isStatic(method.getModifiers())){
                LISTENER_MAP.get(peram).add(new Listener<E>(method, null));
            } else if (!(o instanceof Class)){
                LISTENER_MAP.get(peram).add(new Listener<E>(method, o));
            }
        });
    }
    private static void setAssignableTypes(Class<?> lowest){
        if (lowest == null) return;
        SUPERCLASS_MAP.computeIfAbsent(lowest, l -> {
            List<Class<?>> classes = new ArrayList<>(1);
            if (lowest.getSuperclass() != null) classes.add(lowest.getSuperclass());
            Collections.addAll(classes, lowest.getInterfaces());
            classes.forEach(EventDistributor::setAssignableTypes);
            return classes;
        });
    }
    public static <E extends BotEvent> void distribute(BotEvent event){
        distribute((Class<E>) event.getClass(), (E) event);
    }
    public static <E extends BotEvent> void distribute(Class<E> clazz, E event){
        if (!SUPERCLASS_MAP.containsKey(clazz)){
            setAssignableTypes(clazz);
        }
        if (LISTENER_MAP.containsKey(clazz)) LISTENER_MAP.get(clazz).forEach(listener -> listener.handle(event));
        distributeInner(clazz, event);
    }
    private static <E extends BotEvent> void distributeInner(Class<E> clazz, E event){
        SUPERCLASS_MAP.get(clazz).forEach(c -> {
            if (LISTENER_MAP.containsKey(c)) LISTENER_MAP.get(c).forEach(listener -> listener.handle(event));
            distributeInner((Class<E>) c, event);
        });
    }
    private static class Listener<E extends BotEvent> {
        Method m;
        Object o;
        Listener(Method m, Object o) {
            this.m = m;
            this.o = o;
        }
        void handle(E event){
            try {
                this.m.invoke(this.o, event);
            } catch (IllegalAccessException e) {
                Log.log("This should never happen", e);
            } catch (InvocationTargetException e) {
                Log.log("Error while distributing event: " + this.m.getDeclaringClass().getName() + "#" + this.m.getName() + "\n    " + e.getCause().getMessage(), e);
            }
        }
    }
}
