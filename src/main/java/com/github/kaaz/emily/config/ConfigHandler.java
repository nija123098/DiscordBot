package com.github.kaaz.emily.config;

import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.config.configs.ConfigurableExistsConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.util.Log;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private static final Map<Class<? extends AbstractConfig<?, ? extends Configurable>>, AbstractConfig<?, ? extends Configurable>> CLASS_MAP;
    private static final Map<String, AbstractConfig<?, ? extends Configurable>> STRING_MAP;
    private static final Map<Class<? extends Configurable>, Function<String, ? extends Configurable>> FUNCTION_MAP = new ConcurrentHashMap<>(10);
    static {
        ConfigLevel.load();
        Database.init();
        Set<Class<? extends AbstractConfig>> classes = new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(AbstractConfig.class);
        classes.removeIf(clazz -> Modifier.isAbstract(clazz.getModifiers()));
        CLASS_MAP = new HashMap<>(classes.size() + 2, 1);
        STRING_MAP = new HashMap<>(classes.size() + 2, 1);
        classes.forEach(clazz -> {
            try {
                AbstractConfig config = clazz.newInstance();
                CLASS_MAP.put((Class<? extends AbstractConfig<?, ? extends Configurable>>) clazz, config);
                STRING_MAP.put(config.getName(), config);
            } catch (InstantiationException | IllegalAccessException e) {
                Log.log("Exception during init of a config: " + clazz.getSimpleName(), e);
            }
        });
        add(Track.class, Track::getTrack);
        add(Playlist.class, Playlist::getPlaylist);
        add(User.class, User::getUser);
        add(Channel.class, Channel::getChannel);
        add(GuildUser.class, GuildUser::getGuildUser);
        add(Role.class, Role::getRole);
        add(Guild.class, Guild::getGuild);
        add(GuildUser.class, GuildUser::getGuildUser);
        add(GlobalConfigurable.class, s -> {
            if (!s.equals(GlobalConfigurable.GLOBAL.getID())) return null;
            return GlobalConfigurable.GLOBAL;
        });
        add(Configurable.class, s -> {
            for (int i = 0; i < ConfigLevel.values().length; i++) {
                if (ConfigLevel.values()[i] == ConfigLevel.ALL) continue;
                Configurable configurable = ConfigHandler.getConfigurable(ConfigLevel.values()[i].getType(), s);
                if (configurable != null) return configurable;
            }
            return null;
        });
    }

    private static <T extends Configurable> void add(Class<T> type, Function<String, T> function){
        FUNCTION_MAP.put(type, function);
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Config Handler initialized");
    }

    /**
     * Gets all config instances
     *
     * @return the set of configurable instances
     */
    public static Set<AbstractConfig<?, ? extends Configurable>> getConfigs(){
        return new HashSet<>(CLASS_MAP.values());
    }

    /**
     * Gets config instances for the type
     *
     * @param type the type of configurable
     * @param <T> the type of configurable
     * @return the set of Configs for that type
     */
    public static <T extends Configurable> Set<AbstractConfig<?, T>> getConfigs(Class<T> type){
        return getConfigs().stream().filter(config -> config.getClass().equals(type) || config.getConfigLevel() == ConfigLevel.ALL).map(abstractConfig -> ((AbstractConfig<?, T>) abstractConfig)).collect(Collectors.toSet());
    }

    /**
     * Gets the config object representing a certain config
     * @param configName the config name for the config being gotten
     * @return the object representing the config that is being searched for
     */
    public static AbstractConfig<?, ? extends Configurable> getConfig(String configName){
        return STRING_MAP.get(configName);
    }

    /**
     * Gets the config object representing a certain config
     *
     * @param clazz the class object of the config
     * @return the config that is being represented by the given class
     */
    public static <E extends AbstractConfig<?, ? extends Configurable>> E getConfig(Class<E> clazz){
        Object e = CLASS_MAP.get(clazz);
        if (e != null){
            return (E) e;
        }
        throw new DevelopmentException("Attempted searching for a non-existent config by using Class search: " + clazz.getClass().getName());
    }

    public static <E extends AbstractConfig<?, ? extends Configurable>> long getAge(Class<E> clazz, Configurable configurable){
        return getConfig(clazz).getAge(configurable);
    }

    /**
     * Sets the config value for the given configurable and config
     *
     * @param clazz the class object representing the config
     * @param configurable the configurable the config is to be set for
     * @param value the value the config is being set at
     */
    public static <C extends AbstractConfig<I, T>, I, T extends Configurable> void setExteriorSetting(Class<C> clazz, T configurable, String value){
        getConfig(clazz).setExteriorValue(configurable, value);
    }

    /**
     * Sets the config value for the given configurable and config
     *
     * @param clazz the class object representing the config
     * @param configurable the configurable the config is to be set for
     * @param value the value the config is being set at
     */
    public static <V, T extends Configurable> void setSetting(Class<? extends AbstractConfig<V, T>> clazz, T configurable, V value){
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
        AbstractConfig config = getConfig(configName);
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
     * Uses a function to set the value of the config to a new value.
     *
     * @param clazz the class object representing the config
     * @param configurable the configurable the config is to be set for
     * @param function the function the config gives the old value to and gets a new value from
     */
    public static <V, T extends Configurable> V changeSetting(Class<? extends AbstractConfig<V, T>> clazz, T configurable, Function<V, V> function){
        return getConfig(clazz).changeSetting(configurable, function);
    }

    /**
     * Uses a consumer to set the value of the config to a new value.
     *
     * @param clazz the class object representing the config
     * @param configurable the configurable the config is to be set for
     * @param consumer the consumer the config gives the old value to and gets a new value from
     */
    public static <V, T extends Configurable> V alterSetting(Class<? extends AbstractConfig<V, T>> clazz, T configurable, Consumer<V> consumer){
        return getConfig(clazz).alterSetting(configurable, consumer);
    }

    public static <V, T extends Configurable> V setIfDefault(Class<? extends AbstractConfig<V, T>> clazz, T configurable, Function<V, V> function){
        return getConfig(clazz).setIfDefault(configurable, function);
    }

    public static <V, T extends Configurable> V setIfOld(Class<? extends AbstractConfig<V, T>> clazz, T configurable, long age, Function<V, V> function){
        return getConfig(clazz).setIfOld(configurable, age, function);
    }

    /**
     * Sets the config value for the given configurable and config
     *
     * @param configName the config name of the config to be set
     * @param configurable the configurable the config is to be set for
     * @param value the value to be set
     * @return if the value is set
     */
    public static boolean setExteriorSetting(String configName, Configurable configurable, String value){
        AbstractConfig config = getConfig(configName);
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
    public static <I, T extends Configurable> I getSetting(Class<? extends AbstractConfig<I, T>> clazz, T configurable){
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
    public static <I, T extends Configurable> String getExteriorSetting(Class<? extends AbstractConfig<I, T>> clazz, T configurable){
        return getConfig(clazz).getExteriorValue(configurable);
    }

    /**
     * A setter for a config for a given configurable
     *
     * @param configName the name of the config to be gotten
     * @param configurable the configurable that the config value
     *                     is to be gotten for
     * @return the value of the config for the configurable
     */
    public static Object getSetting(String configName, Configurable configurable){
        AbstractConfig config = getConfig(configName);
        if (config != null){
            return config.getValue(configurable);
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
     * @return the value of the config for the configurable
     */
    public static String getExteriorSetting(String configName, Configurable configurable){
        AbstractConfig config = getConfig(configName);
        if (config != null){
            return config.getExteriorValue(configurable);
        } else {
            return null;
        }
    }

    public static <T extends Configurable, V> Map<T, V> getNonDefaultSettings(Class<? extends AbstractConfig<V, T>> config){// SQL
        return getConfig(config).getNonDefaultSettings();
    }

    /**
     * Gets the count of that type according to database memory
     *
     * @param type the type of instance to get the count for
     * @return the count of the type instances
     */
    public static int getTypeCount(Class<? extends Configurable> type){
        return getNonDefaultSettings(ConfigurableExistsConfig.class).size();
    }

    /**
     * Gets a function to get a instance
     * of the specified type from the id
     *
     * @param type the class type to get the function for
     * @param <T> the configurable type
     * @return the function to get a instance of the type from an id
     */
    public static <T extends Configurable> Function<String, T> getIDFunction(Class<T> type){
        return (Function<String, T>) FUNCTION_MAP.get(type);
    }

    /**
     * Gets the configurable from the type and arguments
     *
     * @param type the type of configurable
     * @param id the id of the configurable
     * @param <T> the type of configurable
     * @return the configurable according to the id
     */
    public static <T extends Configurable> T getConfigurable(Class<T> type, String id){
        return getIDFunction(type).apply(id);
    }

    /**
     * Gets the configurable from the type and arguments
     *
     * @param id the id of the configurable
     * @return the configurable according to the id
     */
    public static Configurable getConfigurable(String id){
        Configurable c;
        for (Function<String, ? extends Configurable> f : FUNCTION_MAP.values()){
            if ((c = f.apply(id)) != null) return c;
        }
        return null;// throw new ArgumentException("That ID matches no configurables");
    }

    /**
     * Gets the list of the type starting at the index
     * and with the next count of elements specified by size
     *
     * @param type the type of
     * @param start the start index of the type
     * @param size the size of the list to get instances of.
     *             The size will be adjusted if it is to big
     * @param <T> the configurable type
     * @return a list of the configurable types
     * starting with the start index and ending
     * with the start index plus size
     */
    public static <T extends Configurable> List<T> getTypeInstances(Class<T> type, long start, int size){
        Function<String, T> function = getIDFunction(type);
        int typeCount = getTypeCount(type);
        if (typeCount < (start + size)){
            size = (int) (typeCount - start);
        }
        return getTypeIDs(type, start, size).stream().map(function).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Gets a list of all instances of a configurable
     *
     * @param type the type of configurable
     * @param <T> the type of configurable
     * @return all instances of the configurable
     */
    public static <T extends Configurable> List<T> getTypeInstances(Class<T> type){
        return getTypeInstances(type, 0, getTypeCount(type));
    }

    /**
     * Gets the list of the type starting at the index
     * and with the next count of elements specified by size
     *
     * @param type the type of
     * @param start the start index of the type
     * @param size the size of the list to get instances of
     * @return a list of the string ids to the
     * type starting with the start index and
     * ending with the start index plus size
     */
    private static List<String> getTypeIDs(Class<? extends Configurable> type, long start, int size){
        List<String> list = new ArrayList<>(size);
        AtomicLong s = new AtomicLong(start);
        getNonDefaultSettings(ConfigurableExistsConfig.class).forEach((configurable, aBoolean) -> {
            if (s.decrementAndGet() <= 0 && list.size() < size) list.add(configurable.getID());
        });
        return list;
    }

    /**
     * Moves the values of this config to the new ID
     * and returns the new config for those values
     *
     * @param newID the id to move the config's values to
     * @return the new Configurable instance
     */
    public static <T extends Configurable> T moveID(T oldConfig, String newID){// SQL
        return ConfigHandler.getConfigurable((Class<T>) oldConfig.getClass(), newID);
    }
}
