/**
 * Made by Celestialdeath99 on 11/4/2017.
 */

package com.github.nija123098.evelyn.util;

public class PlatformDetector {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() { return (OS.contains("mac")); }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }


    public static String ConvertPath(String path) {
        StringBuffer result = new StringBuffer(path.length());
        char from = '\\';
        char to = '/';

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == from) {
                result.append(to);
            } else {
                result.append(path.charAt(i));
            }
        }
        return result.toString();
    }

}
