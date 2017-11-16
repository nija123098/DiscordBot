package com.github.nija123098.evelyn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Celestialdeath99
 */

public class ExecuteShellCommand {
    static String output;
    public static void commandToExecute(String command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        String line;
        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) !=null) {
                builder.append(line).append("\n");
            }
            output = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getOutput() {
        return output;
    }
}