package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exception.DevelopmentException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class InitBuffer<E> {
    private final Thread thread;
    private final BlockingQueue<E> buffer;

    public InitBuffer(int bufferSize, Supplier<E> supplier) {
        this.buffer = new ArrayBlockingQueue<>(bufferSize);
        this.thread = ThreadHelper.getDemonThread(() -> {
            while (true) {
                while (this.buffer.size() < bufferSize) this.buffer.offer(supplier.get());
                CareLess.lessSleep(Integer.MAX_VALUE);
            }
        }, "Init-Buffer");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public E get() {
        try {
            this.thread.interrupt();
            return this.buffer.take();
        } catch (InterruptedException e) {
            throw new DevelopmentException(e);
        }
    }

    public void give(E e) {
        this.buffer.offer(e);
    }

    public void borrow(Consumer<E> consumer) {
        E val = this.get();
        consumer.accept(val);
        this.give(val);
    }
}
