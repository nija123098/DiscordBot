package com.github.nija123098.evelyn.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple utility for buffering IO calls in order to
 * not be rate limited or break things.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class CallBuffer {
    private final ScheduledExecutorService scheduledExecutorService;
    private final AtomicLong time = new AtomicLong(System.currentTimeMillis());
    private final long callDifference;

    /**
     * Builds a instance with the specified callDifference.
     *
     * @param callDifference the deference in.
     */
    public CallBuffer(String name, long callDifference) {
        this.callDifference = callDifference;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, name + "-Call-Buffer-Thread");
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * Does not guarantee the Runnable is ever ran.
     *
     * @param runnable the thing to eventually run.
     * @return the amount of time in millis that it will take to complete.
     */
    public long call(Runnable runnable) {
        long val = this.time.get(), current = System.currentTimeMillis();
        this.time.set(val > current ? val + this.callDifference : current);
        this.scheduledExecutorService.schedule(runnable, this.time.get() - current, TimeUnit.MILLISECONDS);
        return current - this.time.get();
    }

    public long getDelay() {
        return System.currentTimeMillis() - this.time.get();
    }
}
