package com.github.nija123098.evelyn.util;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class CacheHelper {
    private static final ScheduledExecutorService REMOVAL_EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Cache-Removal-Thread"));
    public static class ContainmentCache<V> implements Iterable<V> {
        public ContainmentCache(long delay) {
            this.delay = delay;
        }
        private final long delay;
        private final Map<V, ScheduledFuture<?>> vMap = new HashMap<>();
        public boolean contains(V v) {
            return this.vMap.containsKey(v);
        }
        public boolean add(V v) {
            ScheduledFuture<?> future = this.vMap.put(v, REMOVAL_EXECUTOR.schedule(() -> this.vMap.remove(v), this.delay, TimeUnit.MILLISECONDS));
            if (future == null) return true;
            future.cancel(false);
            return false;
        }
        public void remove(V v) {
            ScheduledFuture<?> future = this.vMap.remove(v);
            if (future != null) future.cancel(false);
        }

        @Override
        public Iterator<V> iterator() {
            return this.asArrayList().iterator();
        }

        public List<V> asArrayList() {
            return new ArrayList<>(this.vMap.keySet());
        }
    }
    public static class ReferenceCache<V> {// null not supported
        public ReferenceCache(long delay, Supplier<V> supplier) {
            this.delay = delay;
            this.supplier = supplier;
        }
        private final long delay;
        private final Supplier<V> supplier;
        private final AtomicReference<V> reference = new AtomicReference<>();
        public V get() {
            if (this.reference.get() == null) {
                this.reference.set(this.supplier.get());
                REMOVAL_EXECUTOR.schedule(() -> this.reference.set(null), this.delay, TimeUnit.MILLISECONDS);
            }
            return this.reference.get();
        }
    }
}
