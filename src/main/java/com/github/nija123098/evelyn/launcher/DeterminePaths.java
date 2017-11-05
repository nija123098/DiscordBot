package com.github.nija123098.evelyn.launcher;

import com.github.nija123098.evelyn.util.PlatformDetector;

public class DeterminePaths {

    public static String PathEnding() {
        String fileEnding = null;
        if (PlatformDetector.isUnix()) {
            fileEnding = "/";
        }
        else if (PlatformDetector.isWindows()) {
            fileEnding = "\\";
        }
        return fileEnding;
    }
}
