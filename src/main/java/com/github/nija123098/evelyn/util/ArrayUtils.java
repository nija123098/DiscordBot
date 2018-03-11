package com.github.nija123098.evelyn.util;

import java.util.Arrays;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ArrayUtils {
    public static <V> V[][] copy(V[][] origin) {
        V[][] vs = Arrays.copyOf(origin, origin.length);
        for (int i = 0; i < vs.length; i++) vs[i] = Arrays.copyOf(vs[i], vs[i].length);
        return vs;
    }
    public static <V> V[][] fillNull(V[][] array, V o) {
        for (int i = 0; i < array.length; i++) for (int j = 0; j < array[i].length; j++) if (array[i][j] == null) array[i][j] = o;
        return array;
    }
    public static <V> V[][][] copy(V[][][] origin) {
        V[][][] vs = Arrays.copyOf(origin, origin.length);
        for (int i = 0; i < vs.length; i++) vs[i] = copy(vs[i]);
        return vs;
    }
}
