package com.github.nija123098.evelyn.service.services;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.AbstractService;
import com.github.nija123098.evelyn.util.Log;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class MemoryManagementService extends AbstractService {
    private static final long SERVICE_ITERATION_TIME = 1_000;
    private static final List<ManagedMap<?, ?>> MAPS = new CopyOnWriteArrayList<>();
    private static final List<ManagedList<?>> LISTS = new CopyOnWriteArrayList<>();
    // private static final long[] INDICES = new long[ConfigLevel.values().length];
    // private static final float[] LEFT_OVER = new float[INDICES.length];
    // private static final float[] CONFIG_PER = new float[INDICES.length];
    public MemoryManagementService() {
        super(SERVICE_ITERATION_TIME);
        /*
        Launcher.registerStartup(() -> {
            for (int i = 0; i < INDICES.length; i++) {
                if (ConfigLevel.values()[i] == ConfigLevel.ALL) continue;
                CONFIG_PER[i] = ConfigHandler.getTypeCount(ConfigLevel.values()[i].getType()) / (float) 86_400_000;// 24 hours
            }
        });*/
    }
    @Override
    public void run() {
        MAPS.forEach(ManagedMap::manage);
        LISTS.forEach(ManagedList::manage);
        /* todo fix
        for (int i = 0; i < INDICES.length; i++) {
            if (ConfigLevel.values()[i] == ConfigLevel.ALL) continue;
            try{if (ConfigLevel.values()[i].getType().getMethod("manage").getDeclaringClass().equals(Configurable.class)) continue;
            } catch (NoSuchMethodException e) {Log.log("This should never happen", e);}
            LEFT_OVER[i] += CONFIG_PER[i];
            int count = (int) (LEFT_OVER[i] / 1);
            ConfigHandler.getTypeInstances(ConfigLevel.values()[i].getType(), INDICES[i], count).stream().filter(Objects::nonNull).forEach(Configurable::manage);
            INDICES[i] += count;
            LEFT_OVER[i] %= 1;
        }*/
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
        @Override
        public synchronized boolean add(E e){
            this.times.add(System.currentTimeMillis() + this.persistence);
            return super.add(e);
        }
        @Override
        public synchronized boolean remove(Object e){
            int ind = this.indexOf(e);
            if (ind > -1) this.times.remove(ind);
            return super.remove(e);
        }
        private synchronized void manage(){
            if (this.times.size() == 0) return;
            long currentTime = System.currentTimeMillis();
            while (true){
                if (this.times.size() == 0) return;
                if (currentTime >= this.times.get(0)){
                    this.times.remove(0);
                    this.remove(0);
                }else return;
            }
        }
        @Override
        public synchronized void forEach(Consumer<? super E> action) {
            super.forEach(action);
        }
    }
    public static class ManagedSet<E> extends ConcurrentHashSet<E> {
        private final long persistence;
        private final Map<E, ScheduleService.ScheduledTask> removalTasks = new HashMap<>();
        public ManagedSet(long persistence) {
            this.persistence = persistence;
        }
        @Override
        public boolean add(E e) {
            ScheduleService.ScheduledTask service = this.removalTasks.put(e, ScheduleService.schedule(this.persistence, () -> this.remove(e)));
            if (service != null) service.cancel();
            return super.add(e);
        }
    }
}
