package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exception.ArgumentException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Time {// TODO CLEAN
    private static final Map<Character, Long> TIME_SYMBOLS = new LinkedHashMap<>();

    static {
        TIME_SYMBOLS.put('y', 31536000000L);
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
        if (s.trim().isEmpty()) throw new ArgumentException("No arguments provided for time");
        s = FormatHelper.removeChars(s.toLowerCase(), ' ');
        long val = 0;
        StringBuilder working = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                working.append(s.charAt(i));
            } else if (TIME_SYMBOLS.containsKey(s.charAt(i)) && (working.length() > 0)) {
                val += Integer.parseInt(working.toString()) * TIME_SYMBOLS.get(s.charAt(i));
                working = new StringBuilder();
            } else {
                throw new ArgumentException("Empty option for time symbol");
            }
        }
        if (working.length() != 0) {
            val += Integer.parseInt(s);
        }
        this.time = val + System.currentTimeMillis();
    }

    public long schedualed() {
        return this.time;
    }

    public long timeUntil() {
        return this.time - System.currentTimeMillis();
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
        return time.toInstant(time.atZone(ZoneId.systemDefault()).getOffset()).toEpochMilli();
    }

    public static long toMillis(Instant time) {
        return time.toEpochMilli();
    }

    public static LocalDateTime toLocalDateTime(long time) {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Instant toInstant(long time) {
        return Instant.ofEpochMilli(time);
    }

    public static String getAbbreviatedMusic(long time) {
        int hours = (int) (time / 3_600_000);
        time -= hours * 3_600_000;
        int min = (int) (time / 60_000);
        time -= min * 60_000;
        int sec = (int) (time / 1000);
        String builder = "";
        if (hours > 0) builder = String.valueOf(hours) + ":";
        String current = String.valueOf(min);
        if (current.length() == 1) current = 0 + current;
        builder += current + ":";
        current = String.valueOf(sec);
        if (current.length() == 1) current = 0 + current;
        return builder + current;
    }

    public static String getAbbreviatedMusic(long outOf, long current) {
        return getAbbreviatedMusic(current) + "/" + getAbbreviatedMusic(outOf);
    }
}
