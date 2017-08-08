package com.github.kaaz.emily.config;

import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.util.LanguageHelper;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.ReflectionHelper;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class TypeChanger {
    private static final XStream X_STREAM = new XStream();
    static {
        X_STREAM.aliasPackage("emily-package", Reference.BASE_PACKAGE);
        //XStream.setupDefaultSecurity(X_STREAM);
        //ConfigHandler.getConfigs().forEach(config -> X_STREAM.allowTypeHierarchy(config.getValueType()));
    }
    private static final Map<Class<?>, Function<?, String>> TO_STRING = new HashMap<>();
    private static final Map<String, Function<String, ?>> FROM_STRING = new HashMap<>();
    private static final Map<Class<?>, Function<?, ?>> XSTREAM_OBJECT_ALTERING = new HashMap<>();
    private static <T> void add(Class<T> clazz, Function<T, String> toString, Function<String, T> toType){
        TO_STRING.put(clazz, toString);
        FROM_STRING.put(clazz.getName(), toType);
    }
    private static <T> void add(Class<T> clazz, Function<String, T> toType){
        add(clazz, Object::toString, toType);
    }
    static {
        add(Float.class, Float::parseFloat);
        add(Boolean.class, LanguageHelper::getBoolean);
        add(Integer.class, LanguageHelper::getInteger);
        add(Configurable.class, Configurable::getID, ConfigHandler::getConfigurable);
        add(File.class, File::new);
        add(Long.class, Long::parseLong);
        add(Class.class, s -> {
            try{return Class.forName(s);
            } catch (ClassNotFoundException e){
                Log.log("Moved class!", e);
                return null;
            }// Should not happen
        });
    }
    private static <T> void alter(Class<T> clazz, Function<T, Object> function){
        XSTREAM_OBJECT_ALTERING.put(clazz, function);
    }
    static {
        alter(CopyOnWriteArrayList.class, ArrayList::new);
        alter(ConcurrentHashMap.class, HashMap::new);
    }
    private static <T> Object alter(T o){
        Function<T, Object> function = (Function<T, Object>) XSTREAM_OBJECT_ALTERING.get(o.getClass());
        if (function == null) return o;
        return function.apply(o);
    }
    public static boolean normalStorage(Class<?> clazz){
        return clazz.isEnum() || clazz.equals(String.class) || !Collections.disjoint(TO_STRING.keySet(), ReflectionHelper.getAssignableTypes(clazz));
    }
    public static String toString(Class<?> from, Object o){
        if (o == null) return "null";
        if (from.isEnum()) return o.toString();
        if (from.equals(String.class)) return (String) o;
        AtomicReference<String> reference = new AtomicReference<>();
        ReflectionHelper.getAssignableTypes(from).forEach(clazz -> {
            Function<Object, String> f = (Function<Object, String>) TO_STRING.get(clazz);
            if (reference.get() == null && f != null) {
                reference.set(f.apply(o));
            }
        });
        if (reference.get() != null) return reference.get();
        return X_STREAM.toXML(alter(o)).replace("\n", "");
    }
    public static <T> T toObject(Class<T> to, String s){
        if (s.equals("null")) return null;
        if (to.isEnum()) return (T) getEnum(to, s);
        if (to.equals(String.class)) return (T) s;
        AtomicReference<T> reference = new AtomicReference<>();
        ReflectionHelper.getAssignableTypes(to).forEach(clazz -> {
            Function<String, Object> f = (Function<String, Object>) FROM_STRING.get(clazz.getName());
            if (reference.get() == null && f != null) reference.set((T) f.apply(s));
        });
        return reference.get() == null ? (T) X_STREAM.fromXML(s) : reference.get();
    }
    private static <T extends Enum<T>> Object getEnum(Class<?> clazz, String s){
        return Enum.valueOf((Class<T>) clazz, s);
    }
    public static Class<?>[] getRawClasses(Class<?> o){
        Type[] t = ((ParameterizedType) o.getGenericSuperclass()).getActualTypeArguments();
        boolean br = true;
        while (true){
            for (int i = 0; i < t.length; i++) {
                if (t[i] instanceof ParameterizedType){
                    t[i] = ((ParameterizedType) t[i]).getRawType();
                    br = false;
                }
            }
            if (br){
                break;
            }
            br = true;
        }
        Class<?>[] classes = new Class[t.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = (Class<?>) t[i];
        }
        return classes;
    }
}
