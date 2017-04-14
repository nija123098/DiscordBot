package com.github.kaaz.emily.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class Rand {
    private static final Random RANDOM = new Random();
    public static int getRand(int max){// 0 inclusive
        return max == 0 ? 0 : Math.abs(RANDOM.nextInt()) % (max + 1);
    }
    public static int getRand(int max, Integer...exclude){// todo optimize
        Set<Integer> excludes = new HashSet<>();
        Collections.addAll(excludes, exclude);
        int val;
        if (exclude.length - 1 > max){
            return 0;
        }
        while (true){
            val = getRand(max);
            if (!excludes.contains(val)){
                return val;
            }
        }
    }
}
