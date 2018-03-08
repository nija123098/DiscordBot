package com.github.nija123098.evelyn.util;

/**
 * Where good practice goes to die.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class CareLess {
    public static void something(Care care) {
        try {
            care.less();
        } catch (Throwable ignored) {
        }
    }

    @FunctionalInterface
    public interface Care {
        void less() throws Throwable;
    }

    public static void lessSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static boolean getBoolean(Boolean bool) {
        return bool == null ? false : bool;
    }
}
