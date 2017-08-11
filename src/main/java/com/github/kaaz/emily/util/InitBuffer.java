package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.DevelopmentException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Made by nija123098 on 6/27/2017.
 */
public class InitBuffer<E> {
    private final BlockingQueue<E> buffer;
    public InitBuffer(int bufferSize, Supplier<E> supplier) {
        this.buffer = new ArrayBlockingQueue<>(bufferSize);
        Thread thread = new Thread(() -> Care.less(() -> {
            while (this.buffer.size() < bufferSize) this.buffer.offer(supplier.get());
            Thread.sleep(1_000);
        }), "InitBufferThread-" + this.hashCode());
        thread.setDaemon(true);
        thread.start();
    }
    public E get(){
        try{return this.buffer.take();
        } catch (InterruptedException e) {
            throw new DevelopmentException(e);
        }
    }
    public void give(E e){
        this.buffer.offer(e);
    }
    public void borrow(Consumer<E> consumer){
        E val = this.get();
        consumer.accept(val);
        this.give(val);
    }
}
