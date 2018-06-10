package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import sun.nio.ch.Interruptible;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadHelper {
    private static final Map<String, Integer> THREAD_NUMBER_MAP = new ConcurrentHashMap<>();
    public static Thread getDemonThreadSingle(Runnable runnable, String name) {
        name += "-" + THREAD_NUMBER_MAP.compute(name, (s, integer) -> integer == null ? 0 : ++integer);
        Thread thread = new Thread(runnable, name);
        thread.setDaemon(true);
        return thread;
    }
    public static Thread getDemonThread(Runnable runnable, String name) {
        name += "-" + THREAD_NUMBER_MAP.compute(name, (s, integer) -> integer == null ? 0 : ++integer);
        Thread thread = new Thread(runnable, name);
        thread.setDaemon(true);
        return thread;
    }
    private static final Set<Thread> SETUP_THREADS = new HashSet<>();
    private static final Set<Thread> ENABLED_THREADS = new HashSet<>();
    public static void enableInterruptLogging(Thread thread) {
        if (!ConfigProvider.BOT_SETTINGS.interruptLogging()) return;
        if (!SETUP_THREADS.contains(thread)) {
            try {// did someone request a horrible hack?
                Method blockedOn = Thread.class.getDeclaredMethod("blockedOn", Interruptible.class);
                blockedOn.setAccessible(true);
                Field blocker = Thread.class.getDeclaredField("blocker");
                blocker.setAccessible(true);
                Field blockerLock = Thread.class.getDeclaredField("blockerLock");
                blockerLock.setAccessible(true);
                synchronized (blockerLock.get(thread)) {
                    blockedOn.invoke(thread, new LoggingInterruptible((Interruptible) blocker.get(thread)));
                }
            } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                Log.log("Exception thrown enabling interrupt logging, aborting without consequence", e);
            }
            SETUP_THREADS.removeIf(t -> !t.isAlive());// allow GC on thread objects
            SETUP_THREADS.add(thread);
        }
        ENABLED_THREADS.add(thread);
    }
    public static void disableInterruptLogging(Thread thread) {
        if (!ConfigProvider.BOT_SETTINGS.interruptLogging()) return;
        ENABLED_THREADS.remove(thread);
    }
    private static class LoggingInterruptible implements Interruptible {
        private Interruptible interruptible;
        public LoggingInterruptible(Interruptible interruptible) {
            this.interruptible = interruptible;
        }
        @Override
        public void interrupt(Thread thread) {
            if (ENABLED_THREADS.contains(thread)) {
                Log.log("Thread " + thread.getName() + " being interrupted by " + Thread.currentThread().getName(), new Exception());
            }
            this.interruptible.interrupt(thread);
        }
    }
}
