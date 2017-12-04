package com.github.nija123098.evelyn.util;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class BotClock {

    public static void initialize() {
        Log.log(LogColor.blue("Bot Clock initialized.") + LogColor.yellow(" I don't have time to tell you why I don't have time to initialize."));
    }

    private static Clock clock = null;

    static {
        clock = Clock.systemUTC();
    }

    public static Clock getClock() {
        return clock;
    }

    public static String getDay() {
        return Instant.now(clock).truncatedTo(ChronoUnit.DAYS).toString();
    }

    public static String getSecond() {
        return Instant.now(clock).truncatedTo(ChronoUnit.SECONDS).toString();
    }
}
