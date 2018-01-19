package com.github.nija123098.evelyn.service;

import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.LogColor;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class to handle services,
 * objects that implement Runnable and
 * are called at multiple intervals within
 * a program's runtime.
 *
 * @author nija123098
 * @since 1.0.0
 * @see AbstractService
 */
public class ServiceHandler {
    private static final Map<AbstractService, Long> NORMAL_SERVICES;
    static {
        Set<Class<? extends AbstractService>> classes = new Reflections(Launcher.BASE_PACKAGE).getSubTypesOf(AbstractService.class);
        NORMAL_SERVICES = new HashMap<>();
        final AtomicInteger mayBlockCount = new AtomicInteger();
        long now = System.currentTimeMillis();
        classes.forEach(clazz -> {
            try {
                AbstractService service = clazz.newInstance();
                if (service.getDelayBetween() == -1) return;
                if (service.mayBlock()){
                    Thread thread = new Thread(() -> {
                        long time;
                        while (true){
                            if (service.shouldRun()){
                                time = System.currentTimeMillis();
                                service.run();
                                time = time + service.getDelayBetween() - System.currentTimeMillis();
                                if (time > 0){
                                    try {
                                        Thread.sleep(time + 1);
                                    } catch (InterruptedException e) {
                                        Log.log("Error while sleeping for a service", e);
                                    }
                                }
                            }else{
                                try {
                                    Thread.sleep(service.getDelayBetween());
                                } catch (InterruptedException e) {
                                    Log.log("Error while sleeping for a service", e);
                                }
                            }
                        }
                    }, "Service-Handler-Thread-" + mayBlockCount.incrementAndGet());
                    thread.setDaemon(true);
                    Launcher.registerStartup(thread::start);
                } else NORMAL_SERVICES.put(service, now + service.getDelayBetween());
            } catch (Exception e){
                Log.log("Failed to init service: " + clazz.getSimpleName(), e);
            }
        });
        Launcher.registerStartup(() -> NORMAL_SERVICES.keySet().forEach(Runnable::run));
        Thread thread = new Thread(() -> {
            final AtomicLong delta = new AtomicLong(), least = new AtomicLong(Long.MAX_VALUE), current = new AtomicLong(System.currentTimeMillis());
            Set<AbstractService> toRun = new HashSet<>();
            while (true){
                toRun.clear();
                current.set(System.currentTimeMillis());
                least.set(Long.MAX_VALUE);
                NORMAL_SERVICES.forEach((abstractService, nextRun) -> {
                    delta.set(nextRun - current.get());
                    if (delta.get() < least.get()) least.set(delta.get());
                    if (delta.get() <= 0) toRun.add(abstractService);
                });
                toRun.forEach(Runnable::run);
                toRun.forEach(abstractService -> NORMAL_SERVICES.put(abstractService, current.get() + abstractService.getDelayBetween()));
                try {
                    if (least.get() > 0) Thread.sleep(least.get());
                } catch (InterruptedException e) {
                    Log.log("Error thrown during sleeping time between normal service handler runs", e);
                }
            }
        }, "Service-Handler-Thread-0");
        thread.setDaemon(true);
        Launcher.registerStartup(thread::start);
    }

    /**
     * Forces the initialization of this class.
     */
    public static void initialize(){
        Log.log(LogColor.blue("Service Handler initialized.") + LogColor.yellow(" The tertiary sector or service sector is the third of the four economic sectors of the four-sector theory."));
    }
}
