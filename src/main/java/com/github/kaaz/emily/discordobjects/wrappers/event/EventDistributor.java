package com.github.kaaz.emily.discordobjects.wrappers.event;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.exeption.GhostException;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.ReflectionHelper;
import com.github.kaaz.emily.util.ThreadProvider;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 3/12/2017.
 */
public class EventDistributor {
    private static final Map<Class<?>, Set<Listener>> LISTENER_MAP = new ConcurrentHashMap<>();
    public static <E extends BotEvent> void register(Object o){
        Class<?> clazz = o instanceof Class ? (Class<?>) o : o.getClass();
        Stream.of(clazz.getMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).filter(method -> method.getParameterCount() == 1).filter(method -> BotEvent.class.isAssignableFrom(method.getParameterTypes()[0])).forEach(method -> {
            Class<E> peram = (Class<E>) method.getParameterTypes()[0];
            Set<Listener> listeners = LISTENER_MAP.computeIfAbsent(peram, cl -> new ConcurrentHashSet<>());
            if (Modifier.isStatic(method.getModifiers())){
                listeners.add(new Listener<E>(method, null));
            } else if (!(o instanceof Class)){
                listeners.add(new Listener<E>(method, o));
            } else throw new DevelopmentException("Unknown event listener type");// Check if the listener is static
        });
    }
    public static <E extends BotEvent> void distribute(E event){
        ThreadProvider.submit(() -> distribute((Class<E>) event.getClass(), event));
    }
    public static <E extends BotEvent> void distribute(Class<E> clazz, E event){
        ReflectionHelper.getAssignableTypes(clazz).forEach(c -> {
            Set<Listener> listeners = LISTENER_MAP.get(c);
            try{if (listeners != null) listeners.forEach(listener -> listener.handle(event));
            } catch (Exception e){
                if (GhostException.isGhostCaused(e)) return;
                throw e;
            }
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
                if (GhostException.isGhostCaused(e.getCause())) return;
                Log.log("Error while distributing event: " + this.m.getDeclaringClass().getName() + "#" + this.m.getName() + " - " + e.getCause().getMessage(), e);
            }
        }
    }
}
