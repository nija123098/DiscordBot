package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class Care {
    public static void less(Less less){
        try{less.care();
        }catch(Throwable ignored){}
    }
    @FunctionalInterface
    public interface Less {
        void care() throws Throwable;
    }
    public static void lessSleep(long millis){
        Care.less(() -> Thread.sleep(millis));
    }
}
