package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exception.ArgumentException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.nija123098.evelyn.util.FormatHelper.removeChars;
import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneId.systemDefault;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Time {// TODO CLEAN
    private static final Map<Character, Long> TIME_SYMBOLS = new LinkedHashMap<>();

    static {
        TIME_SYMBOLS.put('w', 604800000L);
        TIME_SYMBOLS.put('d', 86400000L);
        TIME_SYMBOLS.put('h', 3600000L);
        TIME_SYMBOLS.put('m', 60000L);
        TIME_SYMBOLS.put('s', 1000L);
    }

    private long time;

    public Time(long time) {
        this.time = time;
    }

    public Time(String s) {
        s = removeChars(s.toLowerCase(), ' ');
        long val = 0;
        String working = "";
        for (int i = 0; i < s.length(); i++) {
            if (isDigit(s.charAt(i))) {
                working += s.charAt(i);
            } else if (TIME_SYMBOLS.containsKey(s.charAt(i)) && !working.isEmpty()) {
                val += parseInt(working) * TIME_SYMBOLS.get(s.charAt(i));
                working = "";
            } else {
                throw new ArgumentException("Empty option for time symbol");
            }
        }
        if (working.length() != 0) {
            val += parseInt(s);
        }
        this.time = val + currentTimeMillis();
    }

    public long schedualed() {
        return this.time;
    }

    public long timeUntil() {
        return this.time - currentTimeMillis();
    }

    public static String getAbbreviated(long time) {
        if (time < 0) time = -time;
        if (time < 1000) return "0s";
        AtomicLong aTime = new AtomicLong(time);
        StringBuilder builder = new StringBuilder();
        TIME_SYMBOLS.forEach((character, aLong) -> {
            int amount = (int) (aTime.get() / aLong);
            aTime.addAndGet(-amount * aLong);
            if (amount != 0) builder.append(amount).append(character);
        });
        return builder.toString();
    }

    public static long toMillis(LocalDateTime time) {
        return time.toInstant(time.atZone(systemDefault()).getOffset()).toEpochMilli();
    }

    public static LocalDateTime toLocalDateTime(long time) {
        return ofEpochMilli(time).atZone(systemDefault()).toLocalDateTime();
    }

    public static String getAbbreviatedMusic(long time) {
        int hours = (int) (time / 3_600_000);
        time -= hours * 3_600_000;
        int min = (int) (time / 60_000);
        time -= min * 60_000;
        int sec = (int) (time / 1000);
        String builder = "";
        if (hours > 0) builder = valueOf(hours) + ":";
        String current = valueOf(min);
        if (current.length() == 1) current = 0 + current;
        builder += current + ":";
        current = valueOf(sec);
        if (current.length() == 1) current = 0 + current;
        return builder + current;
    }

    public static String getAbbreviatedMusic(long outOf, long current) {
        return getAbbreviatedMusic(current) + "/" + getAbbreviatedMusic(outOf);
    }
}
