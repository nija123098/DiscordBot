package com.github.nija123098.evelyn.util;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class BotClock {

    private static Clock clock;

    static {
        clock = Clock.systemUTC();
        Log.log(LogColor.blue("Bot Clock initialized.") + LogColor.yellow(" I don't have time to tell you why I don't have time to initialize."));
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

    public static String getHMS() {
        String[] time = Instant.now(clock).truncatedTo(ChronoUnit.SECONDS).toString().split("T", 2);
        return time[1].replace("Z", "");
    }

    public static String getYMD() {
        String[] date = Instant.now(clock).truncatedTo(ChronoUnit.DAYS).toString().split("T", 2);
        return date[0];
    }

    public static String getYMDHMS() {
        String[] dnt = Instant.now(clock).truncatedTo(ChronoUnit.SECONDS).toString().split("T", 2);
        return dnt[0] + " " + dnt[1].replace("Z", "");
    }
}
