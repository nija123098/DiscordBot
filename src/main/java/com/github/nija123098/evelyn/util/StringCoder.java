package com.github.nija123098.evelyn.util;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.nija123098.evelyn.util.Log.log;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StringCoder {
    public static String encode(List<String> strings) {
        StringBuilder builder = new StringBuilder(strings.size() + " ");
        strings.forEach(s -> builder.append(s.length()).append(" "));
        builder.append("/");
        strings.forEach(s -> builder.append(s).append("/"));
        return builder.toString();
    }

    public static List<String> decode(String string) {
        try {
            String[] split = string.split(" ");
            int skip = split[0].length() + 1;
            String[] newSplit = string.split("/");
            if (skip + 1 == newSplit.length) {
                String[] ret = copyOfRange(newSplit, 1, newSplit.length);
                return asList(ret);
            }
            int size = parseInt(split[0]);
            List<Integer> sizes = new ArrayList<>(size);
            for (int i = 1; i < size + 1; i++) {
                sizes.add(parseInt(split[i]));
                skip += split[i].length() + 1;
            }
            AtomicReference<String> running = new AtomicReference<>(string.substring(skip + 1));
            List<String> ret = new ArrayList<>(size);
            sizes.forEach(integer -> {
                ret.add(running.get().substring(0, integer));
                running.set(running.get().substring(integer + 1));
            });
            return ret;
        } catch (Exception e) {
            log("A silly Thread tried to decode a string that was not encoded", e);
            throw new InputMismatchException("That is not a encoded string, probably");
        }
    }
}
