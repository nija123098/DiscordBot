package com.github.kaaz.emily.util;

import java.util.*;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class Rand {
    private static final Random RANDOM = new Random();
    public static <E> E getRand(List<E> es, boolean remove){
        int index = Rand.getRand(es.size() - 1);
        return remove ? es.remove(index) : es.get(index);
    }
    public static int getRand(int max){// 0 inclusive
        return max < 1 ? 0 : Math.abs(RANDOM.nextInt()) % (max + 1);
    }
    public static Integer getRand(int max, Integer...exclude){
        if (exclude.length == 0 || exclude.length > max) return getRand(max);
        Map<Integer, Integer> map = new HashMap<>(max - exclude.length);
        Set<Integer> excludes = new HashSet<>();
        Collections.addAll(excludes, exclude);
        int current = -1;
        for (int i = 0; i < max + 1; ++i) {
            if (excludes.contains(i)) {
                continue;
            }
            map.put(++current, i);
        }
        return map.get(getRand(map.size() - 1));
    }
}
