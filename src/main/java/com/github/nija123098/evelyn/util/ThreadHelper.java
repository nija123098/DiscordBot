package com.github.nija123098.evelyn.util;

import sun.nio.ch.Interruptible;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
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
    public static void enableInterruptLogging(Thread thread) {
        try {// did someone request a horrible hack?
            Method blockedOn = Thread.class.getDeclaredMethod("blockedOn", Interruptible.class);
            blockedOn.setAccessible(true);
            Field blocker = Thread.class.getDeclaredField("blocker");
            blocker.setAccessible(true);
            Interruptible interruptible = (Interruptible) blocker.get(thread);
            if (interruptible instanceof LoggingInterruptible) return;
            Field blockerLock = Thread.class.getDeclaredField("blockerLock");
            blockerLock.setAccessible(true);
            synchronized (blockerLock.get(thread)) {
                blockedOn.invoke(thread, new LoggingInterruptible(interruptible));
            }
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            Log.log("Exception thrown enabling interrupt logging, aborting without consequence", e);
        }
    }
    private static class LoggingInterruptible implements Interruptible {
        private Interruptible interruptible;
        public LoggingInterruptible(Interruptible interruptible) {
            this.interruptible = interruptible;
        }
        @Override
        public void interrupt(Thread thread) {
            Log.log("Thread " + thread.getName() + " being interrupted by " + Thread.currentThread().getName(), new Exception());
            this.interruptible.interrupt(thread);
        }
    }
}
