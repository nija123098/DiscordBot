package com.github.nija123098.evelyn.service.services;

import com.github.nija123098.evelyn.service.AbstractService;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadProvider;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class ScheduleService extends AbstractService {
    private static final Map<Long, Set<ScheduledTask>> SERVICE_MAP = new HashMap<>();
    private final AtomicLong I = new AtomicLong(System.currentTimeMillis());
    public ScheduleService() {// this should be made a helper
        super(-1);// should commit unique thread
        Thread thread = new Thread(() -> {
            while (true) this.run();
        }, "Schedule-Service-Thread");
        thread.setDaemon(true);
        thread.start();
    }
    public static ScheduledTask schedule(long delay, Runnable runnable){
        return new ScheduledTask(delay, runnable);
    }
    public static ScheduledRepeatedTask scheduleRepeat(long delay, long delayBetween, Runnable runnable){
        return new ScheduledRepeatedTask(delay, runnable, delayBetween);
    }
    @Override
    public void run() {
        if (I.get() > System.currentTimeMillis()) {
            Care.lessSleep(0);
            return;
        }
        Set<ScheduledTask> tasks = SERVICE_MAP.remove(I.getAndIncrement());
        if (tasks != null) tasks.forEach(scheduledTask -> ThreadProvider.sub(scheduledTask::run));
    }
    public static class ScheduledTask {
        private boolean cancel;
        private long time;
        private Runnable runnable;
        private ScheduledTask(long time, Runnable runnable) {
            this.time = time + System.currentTimeMillis();
            this.runnable = runnable;
            if (time < 4){
                this.run();
                return;
            }
            SERVICE_MAP.computeIfAbsent(this.time, l -> new ConcurrentHashSet<>()).add(this);
        }
        public void cancel(){
            this.cancel = true;
            Set<ScheduleService.ScheduledTask> services = SERVICE_MAP.get(this.time);
            if (services != null) services.remove(this);// if null it is too late to cancel
        }
        public boolean isCanceled(){
            return this.cancel;
        }
        boolean run(){
            if (!this.cancel){
                try{runnable.run();
                } catch (Throwable t){
                    Log.log("Exception running ScheduledService", t);
                }
                return true;
            }
            this.cancel();
            return false;
        }
    }
    public static class ScheduledRepeatedTask extends ScheduledTask {
        private boolean skipping;
        private long delayBetween;
        private ScheduledRepeatedTask(long time, Runnable runnable, long delayBetween) {
            super(time, runnable);
            this.delayBetween = delayBetween;
        }
        @Override
        boolean run(){
            if (super.run() && !this.skipping){
                SERVICE_MAP.computeIfAbsent(this.delayBetween + System.currentTimeMillis(), l -> new ConcurrentHashSet<>()).add(this);
                return true;
            }
            return false;
        }
        @Override
        public void cancel(){
            super.cancel = true;
        }
        public void setSkipping(boolean skipping){
            this.skipping = skipping;
        }
    }
}
