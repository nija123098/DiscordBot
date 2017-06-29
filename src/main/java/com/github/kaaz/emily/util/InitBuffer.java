package com.github.kaaz.emily.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Made by nija123098 on 6/27/2017.
 */
public class InitBuffer<E> {
    private final AtomicInteger size;
    private final List<E> buffer;
    public InitBuffer(int bufferSize, Supplier<E> supplier) {
        this.buffer = new ArrayList<>(bufferSize);
        this.size = new AtomicInteger(bufferSize);
        Thread thread = new Thread(() -> Care.less(() -> {
            while (this.buffer.size() < size.get()) this.buffer.add(supplier.get());
            Thread.sleep(1_000);
        }), "InitBufferThread-" + this.hashCode());
        thread.setDaemon(true);
        thread.start();
    }
        public E get(){
        while (this.buffer.isEmpty()) Care.less(() -> Thread.sleep(500));
        return this.buffer.remove(0);
    }
    public void give(E e){
        this.buffer.add(e);
    }
    public void borrow(Consumer<E> consumer){
        E val = this.get();
        consumer.accept(val);
        this.give(val);
    }
    public void setBufferSize(int size){
        this.size.set(size);
    }
}
