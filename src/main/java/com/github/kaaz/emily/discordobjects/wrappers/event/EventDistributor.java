package com.github.kaaz.emily.discordobjects.wrappers.event;

import com.github.kaaz.emily.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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
    private static final Map<Class, Set<Listener>> MAP = new HashMap<>();
    public static <E extends BotEvent> void register(Object o){
        Class<?> clazz;
        if (o instanceof Class){
            clazz = (Class<?>) o;
        } else {
            clazz = o.getClass();
        }
        Stream.of(clazz.getMethods()).filter(method -> method.isAnnotationPresent(EventListener.class)).filter(method -> method.getParameterCount() == 1).filter(method -> method.getParameterTypes()[0].equals(BotEvent.class)).forEach(method -> {
            Class<E> peram = (Class<E>) method.getParameterTypes()[0];
            MAP.computeIfAbsent(peram, cl -> new HashSet<>());
            if (Modifier.isStatic(method.getModifiers())){
                MAP.get(peram).add(new Listener<E>(method, null));
            } else if (!(o instanceof Class)){
                MAP.get(peram).add(new Listener<E>(method, o));
            }
        });
    }
    public static <E extends BotEvent> void distribute(Supplier<E> supplier){
        distribute((Class<E>) ((ParameterizedType) supplier.getClass().getGenericSuperclass()).getActualTypeArguments()[0], supplier);
    }
    private static <E extends BotEvent> void distribute(Class clazz, Supplier<E> supplier){
        if (clazz == null){
            return;
        }
        Set<Listener> set = MAP.get(clazz);
        if (set != null){
            BotEvent botEvent = supplier.get();
            set.forEach(listener -> listener.handle(botEvent));
            distribute(clazz.getSuperclass(), () -> botEvent);
            return;
        }
        distribute(clazz.getSuperclass(), supplier);
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
                Log.log("Error while distributing event", e);
            }
        }
    }
}
