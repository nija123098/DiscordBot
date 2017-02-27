package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.util.Holder;
import com.github.kaaz.discordbot.util.Log;
import org.reflections.Reflections;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class ConfigHandler {
    private static final Map<Class<? extends AbstractConfig>, AbstractConfig<?>> CLASS_MAP;
    private static final Map<String, AbstractConfig<?>>[] STRING_MAP;
    static {
        CLASS_MAP = new HashMap<>();
        STRING_MAP = new Map[ConfigLevel.values().length];
        for (int i = 0; i < STRING_MAP.length; i++) {
            STRING_MAP[i] = new HashMap<>();
        }
        EnumSet.allOf(ConfigLevel.class).forEach(configLevel -> {
            Reflections reflections = new Reflections("com.guthub.kaaz.discordbot.config.configs." + configLevel.name().replace("_", "").toLowerCase());
            Set<Class<? extends AbstractConfig>> classes = reflections.getSubTypesOf(AbstractConfig.class);
            classes.forEach(clazz -> {
                try {
                    AbstractConfig config = clazz.newInstance();
                    config.setConfigLevel(configLevel);
                    CLASS_MAP.put(clazz, config);
                    STRING_MAP[configLevel.ordinal()].put(config.getName(), config);
                } catch (InstantiationException | IllegalAccessException e) {
                    Log.log("Exception during init of a config: " + clazz.getSimpleName(), e);
                }
            });
        });
    }
    public static AbstractConfig getConfig(ConfigLevel level, String configName){
        return STRING_MAP[level.ordinal()].get(configName);
    }
    public static <E extends AbstractConfig> E getConfig(Class<E> clazz){
        Object e = CLASS_MAP.get(clazz);
        if (e != null){
            return (E) e;
        }
        throw new RuntimeException("Attempted searching for a non-existent config by using Class search: " + clazz.getClass().getName());
    }
    public static <E extends AbstractConfig<F>, F> void setSetting(Class<E> clazz, Configurable configurable, F value){
        getConfig(clazz).setValue(configurable, value);
    }
    public static boolean setSetting(String configName, Configurable configurable, Object value){
        AbstractConfig config = getConfig(configurable.getConfigLevel(), configName);
        if (config != null){
            try {
                config.setValue(configurable, value);
                return true;
            } catch (ClassCastException e){
                throw new RuntimeException("Attempted generic value config assignment with the wrong type on config \"" + config.getName() + "\" with value type: " + value.getClass().getName(), e);
            }
        } else {
            return false;
        }
    }
    @SafeVarargs
    public static <E extends AbstractConfig<F>, F> F getSetting(Class<E> clazz, Configurable configurable, Holder<F>...holder){
        Object o = getConfig(clazz).getValue(configurable);
        Holder.fillOptional((F) o, holder);
        return (F) o;
    }
    @SafeVarargs
    public static <E> E getSetting(String configName, Configurable configurable, Holder<E>...holder){
        AbstractConfig config = getConfig(configurable.getConfigLevel(), configName);
        if (config != null){
            try {
                Object o = config.getValue(configurable);
                Holder.fillOptional((E) o, holder);
                return (E) o;
            } catch (ClassCastException e){
                throw new RuntimeException("Attempted to get a value with the wrong holder type for the config: " + config.getName(), e);
            }
        } else {
            return null;
        }
    }
}
