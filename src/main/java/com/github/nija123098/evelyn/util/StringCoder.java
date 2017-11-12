package com.github.nija123098.evelyn.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/23/2017.
 */
public class StringCoder {
    public static String encode(List<String> strings){
        StringBuilder builder = new StringBuilder(strings.size() + " ");
        strings.forEach(s -> builder.append(s.length()).append(" "));
        builder.append("/");
        strings.forEach(s -> builder.append(s).append("/"));
        return builder.toString();
    }
    public static List<String> decode(String string){
        try {
            String[] split = string.split(" ");
            int skip = split[0].length() + 1;
            String[] newSplit = string.split("/");
            if (skip + 1 == newSplit.length){
                String[] ret = Arrays.copyOfRange(newSplit, 1, newSplit.length);
                return Arrays.asList(ret);
            }
            int size = Integer.parseInt(split[0]);
            List<Integer> sizes = new ArrayList<>(size);
            for (int i = 1; i < size + 1; i++) {
                sizes.add(Integer.parseInt(split[i]));
                skip += split[i].length() + 1;
            }
            AtomicReference<String> running = new AtomicReference<>(string.substring(skip + 1));
            List<String> ret = new ArrayList<>(size);
            sizes.forEach(integer -> {
                ret.add(running.get().substring(0, integer));
                running.set(running.get().substring(integer + 1));
            });
            return ret;
        } catch (Exception e){
            Log.log("A silly Thread tried to decode a string that was not encoded", e);
            throw new InputMismatchException("That is not a encoded string, probably");
        }
    }
}
