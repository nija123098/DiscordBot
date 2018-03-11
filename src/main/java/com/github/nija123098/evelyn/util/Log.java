package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import sx.blah.discord.Discord4J;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Log {
    public static final Path LOG_PATH;
    static {
        Path logPath = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
            long time = Files.walk(Paths.get(ConfigProvider.FOLDER_SETTINGS.logsFolder())).map(path -> path.toFile().getName()).map(s -> {
                try {
                    return dateFormat.parse(s).getTime();
                } catch (ParseException e) {
                    return null;
                }
            }).filter(Objects::nonNull).reduce((f, s) -> f > s ? f : s).orElse(0L);
            if (time == 0) throw new IOException("Could not reduce log times");
            logPath = Paths.get(ConfigProvider.FOLDER_SETTINGS.logsFolder(), dateFormat.format(new Date(time)) + ".log");
        } catch (IOException e) {
            Log.log("Exception getting log file", e);
        }
        LOG_PATH = logPath;
    }
    public static void log(String message) {
        Discord4J.LOGGER.info(message);
    }
    public static void log(String message, Throwable t) {
        Discord4J.LOGGER.error(message, t);
    }
}
