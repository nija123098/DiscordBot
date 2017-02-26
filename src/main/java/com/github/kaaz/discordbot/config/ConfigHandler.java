package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.util.Holder;
import com.github.kaaz.discordbot.util.Log;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class ConfigHandler {
    private static final List<AbstractConfig> ABSTRACT_CONFIGS;
    static {
        ABSTRACT_CONFIGS = new ArrayList<>();
        EnumSet.allOf(ConfigLevel.class).forEach(configLevel -> {
            Reflections reflections = new Reflections("com.guthub.kaaz.discordbot.config.configs." + configLevel.name().replace("_", "").toLowerCase());
            Set<Class<? extends AbstractConfig>> classes = reflections.getSubTypesOf(AbstractConfig.class);
            classes.forEach(clazz -> {
                try {
                    AbstractConfig config = clazz.newInstance();
                    config.setConfigLevel(configLevel);
                    ABSTRACT_CONFIGS.add(config);
                } catch (InstantiationException | IllegalAccessException e) {
                    Log.log("Exception during init of a config: " + clazz.getSimpleName(), e);
                }
            });
        });
    }
    public static AbstractConfig getConfig(ConfigLevel level, String configName){
        for (AbstractConfig config : ABSTRACT_CONFIGS) {
            if (config.getConfigLevel() == level && config.getName().equals(configName)) {
                return config;
            }
        }
        return null;
    }
    public static <E extends AbstractConfig> E getConfig(Class<E> clazz){
        for (AbstractConfig config : ABSTRACT_CONFIGS) {
            if (config.getClass().equals(clazz)) {
                return (E) config;
            }
        }
        throw new RuntimeException("Attempted searching for a non-existent config by using Class search: " + clazz.getClass().getName());
    }
    public static <E extends AbstractConfig> void setSetting(Class<E> clazz, Configurable configurable, Object value){
        E config = getConfig(clazz);
        if (config != null){
            config.setValue(configurable, value);
        } else {
            throw new RuntimeException("Attempted searching for a non-existent config by using Class search: " + clazz.getClass().getName());
        }
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
    public static <E extends AbstractConfig, F> F getSetting(Class<E> clazz, Configurable configurable, Holder<F>...holder){
        E config = getConfig(clazz);
        if (config != null){
            try {
                Object o = config.getValue(configurable);
                Holder.fillOptional((F) o, holder);
                return (F) o;
            } catch (ClassCastException e){
                throw new RuntimeException("Attempted to get a config value with the wrong type of holder", e);
            }
        } else {
            throw new RuntimeException("Attempted searching for a non-existent config by using Class search: " + clazz.getClass().getName());
        }
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
                throw new RuntimeException("Attempted", e);
            }
        } else {
            return null;
        }
    }
}
