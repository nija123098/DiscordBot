package com.github.kaaz.emily.util;

import com.github.kaaz.emily.service.services.ScheduleService;

public class CallBuffer {
    private long time = System.currentTimeMillis();
    private long callDifference;
    public CallBuffer(long callDifference) {
        this.callDifference = callDifference;
    }

    /**
     * Does not garentee
     *
     * @param runnable
     */
    public void call(Runnable runnable){
        this.time += this.callDifference;
        ScheduleService.schedule(this.time - System.currentTimeMillis(), runnable);
    }
}
