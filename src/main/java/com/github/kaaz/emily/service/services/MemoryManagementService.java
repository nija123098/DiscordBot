package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.service.AbstractService;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class MemoryManagementService extends AbstractService {
    private static final long SERVICE_ITERATION_TIME = 60000;// 1 min
    private static final Set<ManagedMap<?, ?>> MAPS = new ConcurrentHashSet<>();
    private static final Set<ManagedList<?>> LISTS = new ConcurrentHashSet<>();
    private static final long[] INDICES = new long[ConfigLevel.values().length];
    private static final float[] LEFT_OVER = new float[INDICES.length];
    private static final float[] CONFIG_PER = new float[INDICES.length];
    public MemoryManagementService() {
        super(SERVICE_ITERATION_TIME);
        for (int i = 0; i < INDICES.length; i++) {//43200000, 12 hours
            CONFIG_PER[i] = ConfigHandler.getTypeCount(ConfigLevel.values()[i].getType()) / 43200000;// 12 hours
        }
    }
    @Override
    public void run() {
        MAPS.forEach(ManagedMap::manage);
        LISTS.forEach(ManagedList::manage);
        for (int i = 0; i < INDICES.length; i++) {
            LEFT_OVER[i] += CONFIG_PER[i];
            int count = (int) (LEFT_OVER[i] / 1);
            ConfigHandler.getTypeInstances(ConfigLevel.values()[i].getType(), INDICES[i], count).stream().filter(Objects::nonNull).forEach(Configurable::manage);
            INDICES[i] += count;
            LEFT_OVER[i] %= 1;
        }
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
            LISTS.add(this);
        }
        public synchronized boolean add(E e){
            this.times.add(System.currentTimeMillis() + this.persistence);
            return super.add(e);
        }
        @Override
        public synchronized boolean remove(Object e){
            int ind = this.indexOf(e);
            if (ind > -1){
                this.times.remove(ind);
            }
            return super.remove(e);
        }
        private synchronized void manage(){
            long currentTime = System.currentTimeMillis();
            while (true){
                if (currentTime >= this.times.get(0)){
                    this.remove(0);// index version
                    this.times.remove(0);
                }else{
                    return;
                }
            }
        }
    }
}
