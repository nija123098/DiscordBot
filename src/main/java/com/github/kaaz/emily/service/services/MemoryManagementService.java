package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.service.AbstractService;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class MemoryManagementService extends AbstractService {
    private static final long SERVICE_ITERATION_TIME = 20000;// 20s
    private static final Set<Runnable> RUNNABLES = new ConcurrentHashSet<>();
    private static final Set<ManagedMap<?, ?>> MAPS = new ConcurrentHashSet<>();
    public MemoryManagementService() {
        super(SERVICE_ITERATION_TIME);
    }
    @Override
    public void run() {
        MAPS.forEach(ManagedMap::manage);
        RUNNABLES.forEach(Runnable::run);
    }
    public static void register(Runnable runnable){
        RUNNABLES.add(runnable);
    }
    public static class ManagedMap<K, V> extends ConcurrentHashMap<K, V> {// may want to use a cache
        private int iterationTotal, iteration;
        public ManagedMap(long milliClear) {
            this.iterationTotal = (int) (milliClear / SERVICE_ITERATION_TIME);
            this.iteration = this.iterationTotal;
            MAPS.add(this);
        }
        private void manage(){
            if (--this.iteration == 0){
                this.clear();
                this.iteration = this.iterationTotal;
            }
        }
    }
}
