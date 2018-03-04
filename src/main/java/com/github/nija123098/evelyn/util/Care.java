package com.github.nija123098.evelyn.util;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Care {
    public static void less(Less less) {
        try {
            less.care();
        } catch (Throwable ignored) {
        }
    }

    @FunctionalInterface
    public interface Less {
        void care() throws Throwable;
    }

    public static void lessSleep(long millis) {
        less(() -> Thread.sleep(millis));
    }

    public static boolean lessBoolean(Boolean bool) {
        return bool == null ? false : bool;
    }
}
