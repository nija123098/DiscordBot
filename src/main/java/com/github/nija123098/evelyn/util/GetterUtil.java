package com.github.nija123098.evelyn.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GetterUtil {
    public static <E, F> E getAny(Collection<F> fs, Function<F, E> function) {
        E e;
        for (F f : fs) if ((e = function.apply(f)) != null) return e;
        return null;
    }
    public static <E, F> List<E> getAll(Collection<F> fs, Function<F, Collection<E>> function) {
        List<E> list = new ArrayList<>();
        fs.forEach(f -> list.addAll(function.apply(f)));
        return list;
    }
}
