package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.ArgumentException;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 4/29/2017.
 */
public class Time {
    private static final Map<Character, Long> TIME_SYMBOLS = new HashMap<>();
    static {
        TIME_SYMBOLS.put('w', 604800000L);
        TIME_SYMBOLS.put('d', 86400000L);
        TIME_SYMBOLS.put('h', 3600000L);
        TIME_SYMBOLS.put('m', 60000L);
        TIME_SYMBOLS.put('s', 1000L);
    }
    private long time;
    public Time(String s){
        s = FormatHelper.removeChars(s.toLowerCase(), ' ');
        long val = 0;
        String working = "";
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                working += s.charAt(i);
            } else if (TIME_SYMBOLS.containsKey(s.charAt(i)) && !working.isEmpty()) {
                val += Integer.parseInt(s) * TIME_SYMBOLS.get(s.charAt(i));
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
}
