package com.github.kaaz.emily.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 6/28/2017.
 */
public class ThreadProvider {// upgrade?
    private static int index = -1, available;
    private static final List<Runnable> TASKS;
    static {
        TASKS = new ArrayList<>();
    }
    private static synchronized Runnable getNextTask(){
        return TASKS.isEmpty() ? null : TASKS.remove(0);
    }
    private static void make(){
        ++available;
        Thread thread = new Thread(() -> {
            while (true){
                Runnable runnable = getNextTask();
                if (runnable == null) Care.less(() -> Thread.sleep(500));
                else {
                    --available;
                    runnable.run();
                    ++available;
                }
            }
        }, "ThreadProviderThread-" + ++index);
        thread.setDaemon(true);
        thread.start();
        Log.log("Making thread " + index + " for thread provider", new Exception());
    }
    public static synchronized void submit(Runnable task){
        TASKS.add(task);
        if (available == 0) make();
    }
}
