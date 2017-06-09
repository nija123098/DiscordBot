package com.github.kaaz.emily.config;

import com.github.kaaz.emily.util.LanguageHelper;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.ReflectionHelper;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class TypeChanger {
    private static final XStream X_STREAM = new XStream();
    private static final Map<Class<?>, Function<?, String>> TO_STRING = new HashMap<>();
    private static final Map<Class<?>, Function<String, ?>> FROM_STRING = new HashMap<>();
    private static <T> void add(Class<T> clazz, Function<T, String> toString, Function<String, T> toType){
        TO_STRING.put(clazz, toString);
        FROM_STRING.put(clazz, toType);
    }
    private static <T> void add(Class<T> clazz, Function<String, T> toType){
        add(clazz, Object::toString, toType);
    }
    static {
        add(Float.class, Float::parseFloat);
        add(Boolean.class, LanguageHelper::getBoolean);
        add(Integer.class, LanguageHelper::getInteger);
        add(Configurable.class, ConfigHandler::getConfigurable);
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
    public static boolean normalStorage(Class<?> clazz){
        return clazz.isEnum() || TO_STRING.keySet().contains(clazz);
    }
    public static String toString(Class<?> from, Object o){
        if (o == null) return "null";
        if (from.isEnum()) return o.toString();
        AtomicReference<String> reference = new AtomicReference<>();
        ReflectionHelper.getAssignableTypes(from).forEach(clazz -> {
            Function<Object, String> f = (Function<Object, String>) TO_STRING.get(clazz);
            if (reference.get() == null && f != null) {
                reference.set(f.apply(o));
            }
        });
        if (reference.get() != null) return reference.get();
        return X_STREAM.toXML(o).replace("\n", "");
    }
    public static <T> T toObject(Class<T> to, String s){
        if (s.equals("null")) return null;
        if (to.isEnum()) return (T) getEnum(to, s);
        AtomicReference<T> reference = new AtomicReference<>();
        ReflectionHelper.getAssignableTypes(to).forEach(clazz -> {
            Function<String, Object> f = (Function<String, Object>) FROM_STRING.get(to);
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
