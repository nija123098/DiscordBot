package com.github.nija123098.evelyn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class ExecuteShellCommand {
    static String output;

    public static void commandToExecute(String command) {
        Process p;
        String line;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor(1, TimeUnit.MINUTES);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) !=null) {
                builder.append(line).append("\n");
            }
            output = builder.toString();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getOutput() {
        return output;
    }
}