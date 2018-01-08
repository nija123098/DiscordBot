package com.github.nija123098.evelyn.util;

import java.util.*;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class Rand {
    private static final Random RANDOM = new Random();
    public static <E> E getRand(List<E> es, boolean remove){
        int index = Rand.getRand(es.size());
        return remove ? es.remove(index) : es.get(index);
    }
    public static int getRand(int bound){// 0 inclusive
        return RANDOM.nextInt(bound);
    }
    public static Integer getRand(int bound, Integer...exclude){
        Map<Integer, Integer> map = new HashMap<>(bound - exclude.length + 1, 1);
        Set<Integer> excludes = new HashSet<>();
        Collections.addAll(excludes, exclude);
        if (excludes.size() == 0 || excludes.size() >= bound) return getRand(bound);
        int current = -1;
        for (int i = 0; i < bound; ++i) {
            if (excludes.contains(i)) {
                continue;
            }
            map.put(++current, i);
        }
        return map.get(getRand(map.size()));
    }
}
