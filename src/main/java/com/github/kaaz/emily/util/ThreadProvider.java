package com.github.kaaz.emily.util;

import java.util.concurrent.*;

/**
 * Made by nija123098 on 6/28/2017.
 */
public class ThreadProvider extends ThreadPoolExecutor {// upgrade?
    private static int count = -1;
    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("ThreadProviderGroup");
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadProvider();
    private ThreadProvider() {
        super(64, 256, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000), r -> new Thread(THREAD_GROUP, r, THREAD_GROUP.getName() + "-" + ++count));
    }

    public static ExecutorService getExecutorService() {
        return EXECUTOR_SERVICE;
    }
    public static void sub(Runnable task){
        EXECUTOR_SERVICE.submit(task);
    }
}
