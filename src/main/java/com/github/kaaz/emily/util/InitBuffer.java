package com.github.kaaz.emily.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Made by nija123098 on 6/27/2017.
 */
public class InitBuffer<E> {
    private final List<E> buffer;
    public InitBuffer(int bufferSize, Supplier<E> supplier) {
        this.buffer = new ArrayList<>(bufferSize);
        Thread thread = new Thread(() -> Care.less(() -> {
            while (this.buffer.size() < bufferSize) this.buffer.add(supplier.get());
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
}
