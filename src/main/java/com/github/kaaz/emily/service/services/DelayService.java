package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.service.AbstractService;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class DelayService extends AbstractService {
    private static final List<Triple<Long, Runnable, ScheduledTask>> TRIPLES = new ArrayList<>();
    public DelayService() {
        super(0);
    }
    public static synchronized ScheduledTask schedule(long delay, Runnable runnable){
        ScheduledTask task = new ScheduledTask();
        TRIPLES.add(new ImmutableTriple<>(delay + System.currentTimeMillis(), runnable, task));
        return task;
    }
    public static synchronized ScheduledRepeatedTask scheduleRepeat(long delay, long delayBetween, Runnable runnable){
        ScheduledRepeatedTask task = new ScheduledRepeatedTask(delayBetween);
        TRIPLES.add(new ImmutableTriple<>(delay + System.currentTimeMillis(), runnable, task));
        return task;
    }
    @Override
    public synchronized long getDelayBetween(){
        return TRIPLES.isEmpty() ? 100 : (TRIPLES.get(0).getLeft() - System.currentTimeMillis());
    }
    @Override
    public synchronized void run() {
        if (!TRIPLES.isEmpty() && TRIPLES.get(0).getLeft() <= System.currentTimeMillis() && !TRIPLES.get(0).getRight().isCanceled()){
            TRIPLES.get(0).getMiddle().run();
            TRIPLES.get(0).getRight().ran();
            TRIPLES.remove(0);
        }
    }
    public static class ScheduledTask {
        private boolean cancel;
        private ScheduledTask(){}
        public void cancel(boolean cancel){
            this.cancel = cancel;
        }
        public void chancel(){
            this.cancel = true;
        }
        public boolean isCanceled(){
            return this.cancel;
        }
        void ran(){
        }
    }
    public static class ScheduledRepeatedTask extends ScheduledTask {
        private long delayBetween;
        private ScheduledRepeatedTask(long delayBetween) {
            this.delayBetween = delayBetween;
        }
        @Override
        void ran(){
            scheduleRepeat(this.delayBetween, this.delayBetween, TRIPLES.get(0).getMiddle());
        }
    }
}
