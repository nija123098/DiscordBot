package com.github.kaaz.emily.config;

import com.github.kaaz.emily.util.Holder;
import com.github.kaaz.emily.util.Log;
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
    private static final Map<Class<? extends AbstractConfig>, AbstractConfig<?, ?, ? extends Configurable>> CLASS_MAP;
    private static final Map<String, AbstractConfig<?, ?, ? extends Configurable>> STRING_MAP;
    static {
        CLASS_MAP = new HashMap<>();
        STRING_MAP = new HashMap<>();
        EnumSet.allOf(ConfigLevel.class).forEach(configLevel -> {
            Reflections reflections = new Reflections("com.guthub.kaaz.emily.config.configs." + configLevel.name().replace("_", "").toLowerCase());
            Set<Class<? extends AbstractConfig>> classes = reflections.getSubTypesOf(AbstractConfig.class);
            classes.forEach(clazz -> {
                try {
                    AbstractConfig config = clazz.newInstance();
                    CLASS_MAP.put(clazz, config);
                    STRING_MAP.put(config.getName(), config);
                } catch (InstantiationException | IllegalAccessException e) {
                    Log.log("Exception during init of a config: " + clazz.getSimpleName(), e);
                }
            });
        });
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Config Handler initialized");
    }

    /**
     * Gets the config object representing a certain config
     * @param level the config level for the setting being gotten
     * @param configName the config name for the config being gotten
     * @return the object representing the config that is being searched for
     */
    public static AbstractConfig<?, ?, ? extends Configurable> getConfig(ConfigLevel level, String configName){
        return STRING_MAP.get(configName);
    }

    /**
     * Gets the config object representing a certain config
     *
     * @param clazz the class object of the config
     * @return the config that is being represented by the given class
     */
    public static <E extends AbstractConfig<?, ?, ? extends Configurable>> E getConfig(Class<E> clazz){
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
    public static <C extends AbstractConfig<I, E, T>, E, I, T extends Configurable> void setExteriorSetting(Class<C> clazz, T configurable, E value){
        getConfig(clazz).setExteriorValue(configurable, value);
    }

    /**
     * Sets the config value for the given configurable and config
     *
     * @param clazz the class object representing the config
     * @param configurable the configurable the config is to be set for
     * @param value the value the config is being set at
     */
    public static <I, E, T extends Configurable> void setSetting(Class<? extends AbstractConfig<I, E, T>> clazz, T configurable, I value){
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
     * Sets the config value for the given configurable and config
     *
     * @param configName the config name of the config to be set
     * @param configurable the configurable the config is to be set for
     * @param value the value to be set
     * @return if the value is set
     */
    public static boolean setExteriorSetting(String configName, Configurable configurable, Object value){
        AbstractConfig config = getConfig(configurable.getConfigLevel(), configName);
        if (config != null){
            try {
                config.setExteriorValue(configurable, value);
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
    public static <I, T extends Configurable> I getSetting(Class<? extends AbstractConfig<I, ?, T>> clazz, T configurable){
        return getConfig(clazz).getValue(configurable);
    }

    /**
     * A setter for a config for a given configurable
     *
     * @param clazz the class object that types a config
     * @param configurable the configurable the config
     *                     is to be set for
     * @return the value of the config for the configurable
     */
    public static <I, E, T extends Configurable> E getExteriorSetting(Class<? extends AbstractConfig<I, E, T>> clazz, T configurable){
        return getConfig(clazz).getExteriorValue(configurable);
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
    public static <E> E getExteriorSetting(String configName, Configurable configurable, Holder<E>...holder){
        AbstractConfig config = getConfig(configurable.getConfigLevel(), configName);
        if (config != null){
            try {
                E o = (E) config.getExteriorValue(configurable);
                Holder.fillOptional(o, holder);
                return o;
            } catch (ClassCastException e){
                throw new RuntimeException("Attempted to get a value with the wrong holder type for the config: " + config.getName(), e);
            }
        } else {
            return null;
        }
    }
}
