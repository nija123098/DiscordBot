package com.github.nija123098.evelyn.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class StringChecker {
    private static final Map<String, List<StringChecker>> CASHE = new HashMap<>();
    private static StringChecker getCheckingString(String s, Consumer<String> catchPolicy){
        List<StringChecker> list = CASHE.computeIfAbsent(s, s1 -> new ArrayList<>(2));
        return list.isEmpty() ? new StringChecker(s, catchPolicy) : list.remove(0).setCatchPolicy(catchPolicy);
    }
    public static void checkoutString(String target, Collection<String> check, Consumer<String> catchPolicy){
        Set<StringChecker> stringCheckers = check.stream().map(s -> getCheckingString(s, catchPolicy)).collect(Collectors.toSet());
        new StringIterator(target).forEachRemaining(character -> stringCheckers.forEach(stringChecker -> stringChecker.check(character)));
        stringCheckers.forEach(StringChecker::done);
    }
    private int location = -1;
    private String check;
    private Consumer<String> catchPolicy;
    private StringChecker(String check, Consumer<String> catchPolicy) {
        this.check = check;
        this.catchPolicy = catchPolicy;
    }
    public StringChecker setCatchPolicy(Consumer<String> catchPolicy) {
        this.catchPolicy = catchPolicy;
        return this;
    }
    private void check(char c) {
        if (this.check.charAt(++this.location) != c) this.reset();
        else if (this.location == this.check.length() - 1) {
            this.reset();
            this.catchPolicy.accept(this.check);
        }
    }
    private void reset() {
        this.location = -1;
    }
    private void done(){
        reset();
        this.catchPolicy = null;
        CASHE.get(this.check).add(this);
    }
}
