package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.service.AbstractService;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class MemoryManagementService extends AbstractService {
    private static final long SERVICE_ITERATION_TIME = 20000;// 20s
    private static final Set<ManagedMap<?, ?>> MAPS = new ConcurrentHashSet<>();
    private static final Set<ManagedList<?>> LISTS = new ConcurrentHashSet<>();
    public MemoryManagementService() {
        super(SERVICE_ITERATION_TIME);
    }
    @Override
    public void run() {
        MAPS.forEach(ManagedMap::manage);
        LISTS.forEach(ManagedList::manage);
    }
    public static class ManagedMap<K, V> extends ConcurrentHashMap<K, V> {// may want to use a cache or optimize
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
    public static class ManagedList<E> extends ArrayList<E> {
        private final long persistence;
        private final List<Long> times = new ArrayList<>();
        public ManagedList(long persistence) {
            this.persistence = persistence;
        }
        public boolean add(E e){
            this.times.add(System.currentTimeMillis() + this.persistence);
            return super.add(e);
        }
        private void manage(){
            long currentTime = System.currentTimeMillis();
            while (true){
                if (currentTime >= this.times.get(0)){
                    this.remove(0);
                    this.times.remove(0);
                }else{
                    return;
                }
            }
        }
    }
}
