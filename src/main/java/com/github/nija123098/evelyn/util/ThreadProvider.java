package com.github.nija123098.evelyn.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ThreadProvider extends ThreadPoolExecutor {// upgrade?
    private static int count = -1;
    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("ThreadProviderGroup");
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadProvider();

    private ThreadProvider() {
        super(getRuntime().availableProcessors() * 2, getRuntime().availableProcessors() * 4 + 1, 1, MINUTES, new ArrayBlockingQueue<>(10000), r -> new Thread(THREAD_GROUP, r, THREAD_GROUP.getName() + "-Thread-" + ++count), (r, executor) -> r.run());
    }

    public static ExecutorService getExecutorService() {
        return EXECUTOR_SERVICE;
    }

    public static void sub(Runnable task) {
        EXECUTOR_SERVICE.execute(task);
    }
}
