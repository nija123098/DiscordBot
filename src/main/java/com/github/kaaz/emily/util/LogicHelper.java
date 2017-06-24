package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 6/21/2017.
 */
public class LogicHelper {
    public static boolean oneNotNull(Object...objects){
        boolean found = false;
        for (Object o : objects){
            if (o != null){
                if (found) return false;
            } else found = true;
        }
        return found;
    }
}
