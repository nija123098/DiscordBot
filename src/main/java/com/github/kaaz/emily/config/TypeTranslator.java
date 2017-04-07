package com.github.kaaz.emily.config;

import org.eclipse.jetty.util.ConcurrentHashSet;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Made by nija123098 on 4/4/2017.
 */
public class TypeTranslator {
    private static final Map<Class<?>, TypeTranslation<Object, String>> TO_STRING_FUNCTIONS = new HashMap<>();
    private static final Map<Class<?>, TypeTranslation<String, Object>> FROM_STRING_FUNCTIONS = new HashMap<>();
    static {
        add(Float.class, (in, context) -> in.toString(), (in, context) -> Float.parseFloat(in));
        add(Boolean.class, (in, context) -> in.toString(), (in, context) -> in.equals("!") ? !context : Boolean.parseBoolean(in));
        add(Long.class, (in, context) -> in.toString(), (in, context) -> Long.parseLong(in));
        add(Configurable.class, (in, context) -> in.getID(), (in, context) -> ConfigHandler.getConfigurable(context.getClass(), in));
        add(String.class, (in, context) -> in, (in, context) -> in);
        add(File.class, (in, context) -> in.toString(), (in, context) -> new File(in));
        add(Set.class, (in, context) -> in.toString(), (in, context) -> {
            Type type = context.getClass().getGenericSuperclass();
            Class<?> t = type instanceof ParameterizedType ? (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0] : String.class;
            boolean add = true;
            switch (in.split(" ")[0].toLowerCase()){
                case "remove":
                    add = false;
                case "add":
                    Set<Object> objects = new ConcurrentHashSet<>();
                    objects.addAll(context);
                    if (add){
                        objects.add(toType(in.substring(add ? 3 : 6), t, null));// add can be true
                    }else{
                        objects.remove(toType(in.substring(add ? 3 : 6), t, null));
                    }
                    return objects;
            }
            if (in.startsWith("[") && in.endsWith("]")){
                in = in.substring(1, in.length() - 1);
            }
            String[] parts = in.split(", ");
            Set<Object> objects = new ConcurrentHashSet<>();
            for (String s : parts){
                objects.add(toType(s, t, null));
            }
            return objects;
        });
        add(List.class, (in, context) -> in.toString(), (in, context) -> {
            Type type = context.getClass().getGenericSuperclass();
            Class<?> t = type instanceof ParameterizedType ? (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0] : String.class;
            boolean add = true;
            String[] split = in.split(" ");
            switch (split[0].toLowerCase()){
                case "remove":
                    add = false;
                case "add":
                    List<Object> objects = new CopyOnWriteArrayList<>();
                    objects.addAll(context);
                    if (split.length == 2 && !add){
                        try {
                            objects.remove(Integer.parseInt(split[1]));
                        }catch(NumberFormatException ignored){}// if thrown the user does not mean this option
                    }
                    if (add){
                        objects.add(toType(in.substring(add ? 3 : 6), t, null));// add can be true
                    }else{
                        objects.remove(toType(in.substring(add ? 3 : 6), t, null));
                    }
                    return objects;
            }
            if (in.startsWith("[") && in.endsWith("]")){
                in = in.substring(1, in.length() - 1);
            }
            String[] parts = in.split(", ");
            List<Object> objects = new CopyOnWriteArrayList<>();
            for (String s : parts){
                objects.add(toType(s, t, null));
            }
            return objects;
        });
    }
    private static <T> void add(Class<T> to, TypeTranslation<T, String> fts, TypeTranslation<String, T> ffs){
        TO_STRING_FUNCTIONS.put(to, (TypeTranslation<Object, String>) fts);
        FROM_STRING_FUNCTIONS.put(to, (TypeTranslation<String, Object>) ffs);
    }
    public static String toString(Object in, Class<?> from, String context){
        return TO_STRING_FUNCTIONS.get(from).get(in, context);
    }
    public static <T> T toType(String in, Class<T> to, T context){
        return (T) FROM_STRING_FUNCTIONS.get(to).get(in, context);
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
    @FunctionalInterface
    private interface TypeTranslation<I, O>{
        O get(I in, O context);
    }
}
