package com.github.nija123098.evelyn.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Cache<K, V> implements Map<K, V> {
    public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Cache-Remover"));
    private final ConcurrentHashMap<K, FutureValueSet> back;
    private final long millis;
    private final Function<K, V> function;
    private Consumer<K> timeHasCome;

    public Cache(int initialCapacity, long time, Function<K, V> load, BiConsumer<K, V> timeout) {
        this.back = new ConcurrentHashMap<>(initialCapacity, 1);
        this.timeHasCome = timeout == null ? this.back::remove : k -> timeout.accept(k, this.back.remove(k).v);
        this.millis = time;
        if (load == null) this.function = null;
        else this.function = k -> {
            FutureValueSet set = this.back.get(k);
            if (set != null && set.future.cancel(false)) {
                set.future = EXECUTOR_SERVICE.schedule(() -> this.timeHasCome.accept(k), this.millis, TimeUnit.MILLISECONDS);
                return set.v;
            } else {
                V val = load.apply(k);
                this.back.put(k, new FutureValueSet(val, EXECUTOR_SERVICE.schedule(() -> this.timeHasCome.accept(k), this.millis, TimeUnit.MILLISECONDS)));
                return val;
            }
        };
    }

    public Cache(int initialCapacity, long time, Function<K, V> load) {
        this(initialCapacity, time, load, null);
    }

    public V getIfPresent(K key) {
        FutureValueSet set = this.back.get(key);
        return set == null ? null : set.v;
    }

    public void clear() {
        this.back.clear();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.back.size();
    }

    @Override
    public boolean isEmpty() {
        return this.back.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.back.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.back.values().contains(value);
    }

    @Override
    public V get(Object key) {
        if (this.function != null) return this.function.apply((K) key);
        FutureValueSet set = this.back.get(key);
        if (set == null) return null;
        return set.v;
    }

    @Override
    public V put(K key, V val) {
        FutureValueSet set = this.back.get(key);
        set.future.cancel(false);
        FutureValueSet retSet = this.back.put(key, new FutureValueSet(val, EXECUTOR_SERVICE.schedule(() -> this.timeHasCome.accept(key), this.millis, TimeUnit.MILLISECONDS)));
        if (retSet == null) return null;
        return retSet.v;
    }

    @Override
    public V remove(Object key) {
        FutureValueSet set = this.back.get(key);
        if (set == null) return null;
        set.future.cancel(false);
        this.back.remove(key);
        return set.v;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.forEach(this::put);
    }

    class FutureValueSet {
        private FutureValueSet(V v, Future<?> future) {
            this.v = v;
            this.future = future;
        }
        private V v;
        private Future<?> future;
    }
}
