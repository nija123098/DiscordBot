package com.github.nija123098.evelyn.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ConcurrentLoadingHashMap<K, V> extends ConcurrentHashMap<K, V> {
    private Function<K, V> function;
    public ConcurrentLoadingHashMap(Function<K, V> function) {
        this.function = function;
    }

    public ConcurrentLoadingHashMap(int initialCapacity, Function<K, V> function) {
        super(initialCapacity);
        this.function = function;
    }

    public ConcurrentLoadingHashMap(Map<? extends K, ? extends V> m, Function<K, V> function) {
        super(m);
        this.function = function;
    }

    public ConcurrentLoadingHashMap(int initialCapacity, float loadFactor, Function<K, V> function) {
        super(initialCapacity, loadFactor);
        this.function = function;
    }

    public ConcurrentLoadingHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, Function<K, V> function) {
        super(initialCapacity, loadFactor, concurrencyLevel);
        this.function = function;
    }

    @Override
    public V get(Object key) {
        return this.computeIfAbsent((K) key, this.function);
    }

    public V getIfPresent(K key) {
        return this.get(key);
    }
}
