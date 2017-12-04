package com.github.nija123098.evelyn.util;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class LogColor {

    public static String black(String message) {
        return "\u001B[30m" + message +  "\u001B[0m";
    }

    public static String red(String message) {
        return "\u001B[31m" + message + "\u001B[0m";
    }

    public static String green(String message) {
        return "\u001B[32m" + message + "\u001B[0m";
    }

    public static String yellow(String message) {
        return "\u001B[33m" + message + "\u001B[0m";
    }

    public static String blue(String message) {
        return "\u001B[34m" + message + "\u001B[0m";
    }

    public static String magenta(String message) {
        return "\u001B[35m" + message + "\u001B[0m";
    }

    public static String cyan(String message) {
        return "\u001B[36m" + message + "\u001B[0m";
    }

    public static String white(String message) {
        return "\u001B[37m" + message + "\u001B[0m";
    }

}
