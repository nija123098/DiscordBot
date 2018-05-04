package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class ExecuteShellCommand {

    public static String commandToExecute(String command, String filePath) {
        Process p;
        if (filePath == null) filePath = ConfigProvider.BOT_SETTINGS.botFolder();
        try {
            p = Runtime.getRuntime().exec(command, null, new File(filePath));
            p.waitFor(1, TimeUnit.MINUTES);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            IOUtils.copy(p.getInputStream(), stream);
            return stream.toString();
        } catch (InterruptedException | IOException e) {
            throw new DevelopmentException("Exception executing command", e);
        }
    }
}