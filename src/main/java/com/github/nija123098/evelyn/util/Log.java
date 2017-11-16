package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.BotConfig.BotConfig;
import sx.blah.discord.Discord4J;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class Log {
    private static final Path LOG_PATH;
    static {
        String name = "Evelyn-Log-" + System.currentTimeMillis();
        LOG_PATH = Paths.get(BotConfig.LOGS_PATH, name + ".log");
        LOG_PATH.toFile().getParentFile().mkdirs();
        try{Files.write(LOG_PATH, Collections.singletonList("Made log " + name), StandardOpenOption.CREATE);
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    public static void log(String message, Throwable...t){
        try {
            Files.write(LOG_PATH, Collections.singletonList(message), StandardOpenOption.APPEND);
            if (t.length > 0) {
                Discord4J.LOGGER.error(message, t[0]);
                t[0].printStackTrace(new PrintStream(new FileOutputStream(LOG_PATH.toFile(), true)));
            } else Discord4J.LOGGER.info(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
