package com.github.kaaz.discordbot.service;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class ServiceHandler {
    private static final Map<AbstractService, Long> NORMAL_SERVICES;
    static {
        Reflections reflections = new Reflections("com.github.kaaz.discordbot.service");
        Set<Class<? extends AbstractService>> classes = reflections.getSubTypesOf(AbstractService.class);
        NORMAL_SERVICES = new HashMap<>();
        final AtomicInteger mayBlockCount = new AtomicInteger();
        classes.forEach(clazz -> {
            try {
                AbstractService service = clazz.newInstance();
                if (service.mayBlock()){
                    Thread thread = new Thread(() -> {
                        long start;
                        while (true){
                            if (service.shouldRun()){
                                start = System.currentTimeMillis();
                                service.run();
                                start -= service.getDelayBetween();
                                if (start > 0){
                                    try {
                                        Thread.sleep(start);
                                    } catch (InterruptedException e) {
                                        System.out.println("Error while sleeping for a service");
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                try {
                                    Thread.sleep(service.getDelayBetween());
                                } catch (InterruptedException e) {
                                    System.out.println("Error while delaying a service due to should not run");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, "Service-Handler-Thread-" + mayBlockCount.incrementAndGet());
                    thread.setDaemon(true);
                    thread.run();
                } else {
                    NORMAL_SERVICES.put(service, service.getDelayBetween());
                }
            } catch (Exception e){
                System.out.println("Failed to init service: " + clazz.getSimpleName());
                e.printStackTrace();
            }
        });
        NORMAL_SERVICES.keySet().forEach(Runnable::run);
        Thread thread = new Thread(() -> {
            final AtomicLong delta = new AtomicLong(), least = new AtomicLong(Long.MAX_VALUE);
            while (true){
                least.set(Long.MAX_VALUE);
                delta.set(System.currentTimeMillis());
                NORMAL_SERVICES.forEach((service, nextRun) -> {
                    if (nextRun <= 0){
                        service.run();
                    }
                });
                delta.addAndGet(-System.currentTimeMillis());
                delta.set(Math.abs(delta.get()));
                NORMAL_SERVICES.keySet().forEach(service -> NORMAL_SERVICES.put(service, NORMAL_SERVICES.get(service) - delta.get()));
                NORMAL_SERVICES.values().forEach(lo -> {
                    if (lo < least.get()){
                        least.set(lo);
                    }
                });
                try {
                    Thread.sleep(least.get());
                } catch (InterruptedException e) {
                    System.out.println("Error thrown while sleeping time between normal service handler runs");
                    e.printStackTrace();
                }
            }
        }, "Service-Handler-Thread-0");
        thread.setDaemon(true);
        thread.run();
    }
}
