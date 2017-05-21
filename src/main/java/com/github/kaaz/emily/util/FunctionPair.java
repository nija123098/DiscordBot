package com.github.kaaz.emily.util;

import javafx.beans.NamedArg;
import javafx.util.Pair;

import java.util.function.Function;

/**
 * Made by nija123098 on 5/18/2017.
 */
public class FunctionPair<V, K> extends Pair<V, K> {
    public FunctionPair(@NamedArg("key") V key, @NamedArg("key") Function<V, K> value) {
        super(key, value.apply(key));
    }
}
