package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.launcher.Reference;
import com.github.nija123098.evelyn.util.LanguageHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ReflectionHelper;
import com.thoughtworks.xstream.XStream;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Allows for changing a value to a string and back.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class TypeChanger {
    private static final XStream X_STREAM = new XStream();
    public static XStream getXStream() {
        return X_STREAM;
    }
    static {
        XStream.setupDefaultSecurity(X_STREAM);
        X_STREAM.aliasPackage("evelyn-package", Reference.BASE_PACKAGE);
        new Reflections(Reference.BASE_PACKAGE, new SubTypesScanner(false)).getSubTypesOf(Object.class).stream().filter(clazz -> !clazz.isAnnotation()).filter(clazz -> !clazz.isEnum()).filter(clazz -> !clazz.isInterface()).filter(clazz -> !clazz.getName().contains("util")).filter(clazz -> !clazz.getName().contains("$")).filter(clazz -> Stream.of(clazz.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).count() > 0).map(clazz -> {
            Class<?> previous = clazz;
            Class<?> current;
            while (!(current = previous.getSuperclass()).equals(Object.class)) previous = current;
            if (previous.getInterfaces().length == 1) return previous.getInterfaces()[0];
            else return previous;
        }).collect(Collectors.toSet()).forEach(X_STREAM::allowTypeHierarchy);// set reduces repeat
    }
    private static final Map<Class<?>, Function<?, String>> TO_STRING = new HashMap<>();
    private static final Map<String, Function<String, ?>> FROM_STRING = new HashMap<>();
    private static final Map<Class<?>, Function<?, ?>> X_STREAM_OBJECT_ALTERING = new HashMap<>();
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
        X_STREAM_OBJECT_ALTERING.put(clazz, function);
    }
    static {
        alter(List.class, ArrayList::new);
        alter(Map.class, HashMap::new);
    }

    /**
     * Takes care of making the object XStream compatible
     *
     * @param o the object to make compatible.
     * @param <T> the type.
     * @return a new comparable object.
     */
    private static <T> Object alter(T o){
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(o.getClass())){
            Function<T, Object> function = (Function<T, Object>) X_STREAM_OBJECT_ALTERING.get(clazz);
            if (function != null) return function.apply(o);
        }
        return o;
    }

    /**
     * Checks if the given class type can be stored without use of XStream.
     *
     * @param clazz the class type to check.
     * @return if the class type can be stored without using XStream.
     */
    static boolean normalStorage(Class<?> clazz){
        return clazz.isEnum() || clazz.equals(String.class) || !Collections.disjoint(TO_STRING.keySet(), ReflectionHelper.getAssignableTypes(clazz));
    }

    /**
     * Converts a object of the given type to a {@link String}
     * representation that can be converted back.
     *
     * @param from the type the object is expected to be.
     * @param o the object to get a {@link String} representation for.
     * @return the string representation of the object.
     */
    public static String toString(Class<?> from, Object o){
        if (o == null) return "null";
        if (from.isEnum()) return o.toString();
        if (from.equals(String.class)) return (String) o;
        AtomicReference<String> reference = new AtomicReference<>();
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(from)){
            Function<Object, String> f = (Function<Object, String>) TO_STRING.get(clazz);
            if (f != null) return f.apply(o);
        }
        if (reference.get() != null) return reference.get();
        return X_STREAM.toXML(alter(o)).replace("\n", "");
    }

    /**
     * Converts a {@link String} representation
     * of an {@link Object} to an instance.
     *
     * @param to the object type to return.
     * @param s the string representation.
     * @param <T> the type to change the representation to.
     * @return the {@link Object} the {@link String} representation equaled.
     */
    public static <T> T toObject(Class<T> to, String s){
        if (s.equals("null")) return null;
        if (to.isEnum()) return (T) getEnum(to, s);
        if (to.equals(String.class)) return (T) s;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(to)) {
            Function<String, Object> f = (Function<String, Object>) FROM_STRING.get(clazz.getName());
            if (f != null) return (T) f.apply(s);
        }
        return (T) X_STREAM.fromXML(s);
    }
    private static <T extends Enum<T>> Object getEnum(Class<?> clazz, String s){
        return Enum.valueOf((Class<T>) clazz, s);
    }

    /**
     * Gets the class instances of implemented generic types.
     *
     * @param o the class instance to get implemented generic types for.
     * @return a array of implemented generic types.
     */
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
