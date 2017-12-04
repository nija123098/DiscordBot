package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exception.ArgumentException;
import javafx.util.Pair;

import java.util.EnumSet;

import static java.util.Arrays.copyOfRange;
import static java.util.EnumSet.noneOf;
import static java.util.EnumSet.of;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class EnumHelper {
    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> getSet(Class<E> clazz, E... es) {
        switch (es.length) {
            case 0:
                return noneOf(clazz);
            case 1:
                return of(es[0]);
        }
        return of(es[0], copyOfRange(es, 1, es.length));
    }

    public static <E extends Enum<E>> Pair<E, Integer> getValue(Class<E> clazz, String value) {
        value = value.split(" ")[0].toUpperCase();
        for (E e : clazz.getEnumConstants()) {
            if (value.equals(e.name())) return new Pair<>(e, e.name().length());
        }
        throw new ArgumentException("Unknown " + clazz.getSimpleName());
    }

    public static <E extends Enum<E>> Pair<E, Integer> getValue(Object o, String value) {
        return getValue(((Class<E>) o), value);
    }
}
