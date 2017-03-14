package com.github.kaaz.emily.discordobjects.wrappers.event;

import com.github.kaaz.emily.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 3/12/2017.
 */
public class EventDistributor {
    private static final Map<Class, Set<Listener>> MAP;
    static {
        MAP = new HashMap<>();
    }
    public static <E extends DiscordEvent> void register(Object o){
        Class<?> clazz;
        if (o instanceof Class){
            clazz = (Class<?>) o;
        } else {
            clazz = o.getClass();
        }
        Stream.of(clazz.getMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).filter(method -> method.getParameterCount() == 1).filter(method -> method.getParameterTypes()[0].equals(DiscordEvent.class)).forEach(method -> {
            Class<E> peram = (Class<E>) method.getParameterTypes()[0];
            MAP.computeIfAbsent(peram, cl -> new HashSet<>());
            if (Modifier.isStatic(method.getModifiers())){
                MAP.get(peram).add(new Listener<E>(method, null));
            } else if (!(o instanceof Class)){
                MAP.get(peram).add(new Listener<E>(method, o));
            }
        });
    }
    public static <E extends DiscordEvent> void distribute(Class clazz, Supplier<E> supplier){
        if (clazz == null){
            return;
        }
        Set<Listener> set = MAP.get(clazz);
        if (set != null){
            DiscordEvent discordEvent = supplier.get();
            set.forEach(listener -> listener.handle(discordEvent));
            distribute(clazz.getSuperclass(), () -> discordEvent);
            return;
        }
        distribute(clazz.getSuperclass(), supplier);
    }
    private static class Listener<E extends DiscordEvent> {
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
                Log.log("Error while distributing event", e);
            }
        }
    }
}
