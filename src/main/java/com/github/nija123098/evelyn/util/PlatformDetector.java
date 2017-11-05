/**
 * Made by Celestialdeath99 on 11/4/2017.
 */

package com.github.nija123098.evelyn.util;

public class PlatformDetector {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static String fileEnding() {
        String fileEnding = null;
        if (isUnix()) {
            fileEnding = "/";
        }
        else if (isWindows()) {
            fileEnding = "\\";
        }
        return fileEnding;
    }
}
