package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.service.services.ScheduleService;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple utility for buffering IO calls in order to
 * not be rate limited or break things.
 */
public class CallBuffer {
    private final AtomicLong time = new AtomicLong(System.currentTimeMillis());
    private final long callDifference;

    /**
     * Builds a instance with the specified callDifference.
     *
     * @param callDifference the deference in
     */
    public CallBuffer(long callDifference) {
        this.callDifference = callDifference;
    }

    /**
     * Does not guarantee the Runnable is ever ran.
     *
     * @param runnable the thing to eventually run
     */
    public void call(Runnable runnable){
        ScheduleService.schedule(this.time.getAndAdd(this.callDifference) - System.currentTimeMillis() + this.callDifference, runnable);
    }
}