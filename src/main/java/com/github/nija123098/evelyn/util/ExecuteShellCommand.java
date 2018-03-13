package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class ExecuteShellCommand {
    private static String output;

    public static void commandToExecute(String command, String filePath) {
        Runtime rt = Runtime.getRuntime();
        Process p;
        if (filePath==null) { filePath = ConfigProvider.BOT_SETTINGS.botFolder(); }
        String line;
        try {
            p = rt.exec(command, null, new File(filePath));
            p.waitFor(2, TimeUnit.MINUTES);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
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