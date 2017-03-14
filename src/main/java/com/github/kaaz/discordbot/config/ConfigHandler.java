package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.util.Holder;
import com.github.kaaz.discordbot.util.Log;
import org.reflections.Reflections;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The handler for configs values and configurables.
 * This class is the backbone of getting and setting
 * config values for configurable objects.
 *
 * @author nija123098
 * @since 2.0.0
 * @see AbstractConfig
 * @see Configurable
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

    /**
     * Gets the config object representing a certain config
     * @param level the config level for the setting being gotten
     * @param configName the config name for the config being gotten
     * @return the object representing the config that is being searched for
     */
    public static AbstractConfig getConfig(ConfigLevel level, String configName){
        return STRING_MAP[level.ordinal()].get(configName);
    }

    /**
     * Gets the config object representing a certain config
     *
     * @param clazz the class object of the config
     * @return the config that is being represented by the given class
     */
    public static <E extends AbstractConfig> E getConfig(Class<E> clazz){
        Object e = CLASS_MAP.get(clazz);
        if (e != null){
            return (E) e;
        }
        throw new RuntimeException("Attempted searching for a non-existent config by using Class search: " + clazz.getClass().getName());
    }

    /**
     * Sets the config value for the given configurable and config
     *
     * @param clazz the class object representing the config
     * @param configurable the configurable the config is to be set for
     * @param value the value the config is being set at
     */
    public static <E extends AbstractConfig<F>, F> void setSetting(Class<E> clazz, Configurable configurable, F value){
        getConfig(clazz).setValue(configurable, value);
    }

    /**
     * Sets the config value for the given configurable and config
     *
     * @param configName the config name of the config to be set
     * @param configurable the configurable the config is to be set for
     * @param value the value to be set
     * @return if the value is set
     */
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

    /**
     * A setter for a config for a given configurable
     *
     * @param clazz the class object that types a config
     * @param configurable the configurable the config
     *                     is to be set for
     * @return the value of the config for the configurable
     */
    public static <E extends AbstractConfig<F>, F> F getSetting(Class<E> clazz, Configurable configurable){
        Object o = getConfig(clazz).getValue(configurable);
        return (F) o;
    }

    /**
     * A setter for a config for a given configurable
     *
     * @param configName the name of the config to be gotten
     * @param configurable the configurable that the config value
     *                     is to be gotten for
     * @param holder the holder
     * @return the value of the config for the configurable
     */
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
