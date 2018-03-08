package com.github.nija123098.evelyn.util;

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
}
