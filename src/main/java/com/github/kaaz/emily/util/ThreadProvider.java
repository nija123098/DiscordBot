package com.github.kaaz.emily.util;

import java.util.concurrent.*;

/**
 * Made by nija123098 on 6/28/2017.
 */
public class ThreadProvider {// upgrade?
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(64, 256, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000), (ThreadFactory) Thread::new, (r, executor) -> r.run());
    public static synchronized void submit(Runnable task){
        EXECUTOR_SERVICE.submit(task);
    }
}
