package com.github.kaaz.emily.util;

import com.thoughtworks.xstream.XStream;

/**
 * Made by nija123098 on 2/24/2017.
 */
public class SerialHelper {
    private static final XStream X_STREAM = new XStream();
    public static String stringify(Object o){
        return X_STREAM.toXML(o);
    }
    public static Object objectify(String s){
        return X_STREAM.fromXML(s);
    }
}
