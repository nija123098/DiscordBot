package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.ArgumentException;

import java.time.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 4/29/2017.
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
    public Time(long time){
        this.time = time;
    }
    public Time(String s){
        s = FormatHelper.removeChars(s.toLowerCase(), ' ');
        long val = 0;
        String working = "";
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                working += s.charAt(i);
            } else if (TIME_SYMBOLS.containsKey(s.charAt(i)) && !working.isEmpty()) {
                val += Integer.parseInt(working) * TIME_SYMBOLS.get(s.charAt(i));
                working = "";
            }else{
                throw new ArgumentException("Empty option for time symbol");
            }
        }
        if (working.length() != 0) {
            val += Integer.parseInt(s);
        }
        this.time = val + System.currentTimeMillis();
    }
    public long schedualed(){
        return this.time;
    }
    public long timeUntil(){
        return this.time - System.currentTimeMillis();
    }
    public static String getAbbreviated(long time){
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
    public static long toMillis(LocalDateTime time){
        return time.toInstant(time.atZone(ZoneId.systemDefault()).getOffset()).toEpochMilli();
    }
    public static LocalDateTime toLocalDateTime(long time){
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static String getAbbreviatedMusic(long time){
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
    public static String getAbbreviatedMusic(long outOf, long current){
        return getAbbreviatedMusic(current) + "/" + getAbbreviatedMusic(outOf);
    }
}
