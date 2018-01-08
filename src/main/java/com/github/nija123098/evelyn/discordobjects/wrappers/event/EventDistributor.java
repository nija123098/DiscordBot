package com.github.nija123098.evelyn.discordobjects.wrappers.event;

import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.exeption.GhostException;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ReflectionHelper;
import com.github.nija123098.evelyn.util.ThreadProvider;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * A utility class to distribute all {@link BotEvent} instances.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class EventDistributor {
    private static final Map<Class<?>, Set<Listener>> LISTENER_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<Listener>> LISTENER_CASH = new ConcurrentHashMap<>();

    /**
     * Registers a {@link Class} or {@link Object} listener
     * which invokes a {@link Method} with the appropriate
     * {@link BotEvent} and a {@link EventListener} annotation on it.
     *
     * @param o the thing to register.
     * @param <E> the type of event to register.
     */
    public static <E extends BotEvent> void register(Object o){
        Class<?> clazz = o instanceof Class ? (Class<?>) o : o.getClass();
        Stream.of(clazz.getMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).filter(method -> method.getParameterCount() == 1).filter(method -> BotEvent.class.isAssignableFrom(method.getParameterTypes()[0])).forEach(method -> {
            Class<E> peram = (Class<E>) method.getParameterTypes()[0];
            ReflectionHelper.getAssignableTypes(clazz).forEach(LISTENER_CASH::remove);
            LISTENER_CASH.remove(peram);
            Set<Listener> listeners = LISTENER_MAP.computeIfAbsent(peram, cl -> new ConcurrentHashSet<>());
            if (Modifier.isStatic(method.getModifiers())){
                listeners.add(new Listener<E>(method, null));
            } else if (!(o instanceof Class)){
                listeners.add(new Listener<E>(method, o));
            } else throw new DevelopmentException("Unknown event listener type");// Check if the listener is static
        });
    }

    /**
     * Distributes the given event.
     *
     * @param event the event to distribute.
     */
    public static void distribute(BotEvent event){
        ThreadProvider.sub(() -> {
            try{
                LISTENER_CASH.computeIfAbsent(event.getClass(), c -> {
                    List<Listener> listeners = new ArrayList<>();
                    ReflectionHelper.getAssignableTypes(c).forEach(cl -> {
                        Set<Listener> newListeners = LISTENER_MAP.get(cl);
                        if (newListeners != null) listeners.addAll(newListeners);
                    });
                    return listeners;
                }).forEach(listener -> listener.handle(event));
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
