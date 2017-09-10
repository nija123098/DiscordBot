package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.service.services.ScheduleService;

import java.util.concurrent.atomic.AtomicLong;

public class CallBuffer {
    private final AtomicLong time = new AtomicLong(System.currentTimeMillis());
    private final long callDifference;
    public CallBuffer(long callDifference) {
        this.callDifference = callDifference;
    }

    /**
     * Does not guarantee the Runnable is ever ran.
     *
     * @param runnable the thing to eventually run
     */
    public void call(Runnable runnable){
        ScheduleService.schedule(this.time.getAndAdd(this.callDifference) - System.currentTimeMillis(), runnable);
    }
}