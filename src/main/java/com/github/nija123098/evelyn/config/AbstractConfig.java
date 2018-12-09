package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tagable;
import com.github.nija123098.evelyn.tag.Tags;
import com.github.nija123098.evelyn.util.Log;
import com.thoughtworks.xstream.io.StreamException;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The superclass for every config.
 *
 * @author nija123098
 * @since 1.0.0
 * @param <V> The stored type of the config within the database.
 * @param <T> The type of config that this config defines.
 */
@LaymanName(value = "Configuration name", help = "The config name")
public class AbstractConfig<V, T extends Configurable> implements Tagable {
    private final Function<T, V> defaultValue;
    private final String name, displayName, shortId, configCommandDisplay, description;
    private final BotRole botRole;
    private final ConfigLevel configLevel;
    private final ConfigCategory category;
    private final Class<V> valueType;
    private final boolean normalViewing;
    private final Map<T, V> cache;
    private final Set<T> nullCache;
    private final List<Tag> tags;
    public AbstractConfig(String name, String displayName, ConfigCategory category, V defaultValue, String description) {
        this(name, displayName, category, v -> defaultValue, description);
    }
    public AbstractConfig(String name, String displayName, BotRole botRole, ConfigCategory category, V defaultValue, String description) {
        this(name, displayName, botRole, category, v -> defaultValue, description);
    }
    public AbstractConfig(String name, String displayName, ConfigCategory category, Function<T, V> defaultValue, String description) {
        this(name, displayName, category.getBotRole(), category, defaultValue, description);
    }

    /**
     * The constructor to make a config instance.
     *
     * @param name the name of the config, spaces are not allowed.
     * @param displayName the display name of the config, required
     * @param botRole the minimum role allowed to change the value.
     * @param category the catagory to that the config is in.
     * @param defaultValue the function to get the default value for a given config.
     * @param description a description of the config.
     */
    public AbstractConfig(String name, String displayName, BotRole botRole, ConfigCategory category, Function<T, V> defaultValue, String description) {
        this.name = name;
        this.displayName = displayName.isEmpty() ? this.name : displayName;
        this.botRole = botRole;
        this.defaultValue = defaultValue;
        this.description = description;
        this.category = category;
        this.shortId = this.category.addConfig((AbstractConfig<? extends Configurable, ?>) this);
        this.configCommandDisplay = this.displayName + " [" + this.shortId + "]";
        Type[] types = TypeChanger.getRawClasses(this.getClass());
        this.valueType = (Class<V>) types[0];
        if (!ObjectCloner.supports(this.valueType)) throw new DevelopmentException("Cloner does not support type: " + this.valueType.getName());
        this.normalViewing = TypeChanger.normalStorage(this.valueType);
        this.configLevel = ConfigLevel.getLevel((Class<T>) types[types.length - 1]);
        if (this.configLevel.mayCache()) {
            this.cache = new ConcurrentHashMap<>();
            this.nullCache = new HashSet<>();
        }else {
            this.cache = null;
            this.nullCache = null;
        }
        Tags tags = this.getClass().getAnnotation(Tags.class);
        this.tags = tags == null ? Collections.emptyList() : Arrays.asList(tags.value());
        EventDistributor.register(this);
        this.configLevel.getAssignable().forEach(level -> {
            if (level == ConfigLevel.ALL) return;
            try (ResultSet rs = Database.getConnection().getMetaData().getTables(null, null, this.getNameForType(level), null)) {
                while (rs.next()) {
                    String tName = rs.getString("TABLE_NAME");
                    if (tName != null && tName.equals(this.getNameForType(level))) return;
                }// make
                Database.query("CREATE TABLE `" + this.getNameForType(level) + "` (id TINYTEXT, value " + this.getSQLTableType() + ", millis BIGINT)");
            } catch (SQLException e) {
                throw new DevelopmentException("Could not ensure table existence", e);
            }
        });
    }

    /**
     * A standard getter.
     *
     * @return the name of the config.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String typeName() {
        return "config";
    }

    @Override
    public List<Tag> getTags() {
        return this.tags;
    }

    /**
     * A standard getter.
     *
     * @return the short id of the config.
     */
    public String getShortId() {
        return this.shortId;
    }

    /**
     * A standard getter.
     *
     * @return the display name of the config
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * A standard getter.
     *
     * @return the display name with the short id.
     */
    public String getConfigCommandDisplay() {
        return this.configCommandDisplay;
    }

    /**
     * A standard getter.
     *
     * @return the multi-line config description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * A standard getter.
     *
     * @return the bot role required for altering this config value
     */
    public BotRole requiredBotRole() {
        return this.botRole;
    }

    /**
     * A standard getter.
     *
     * @return the default value of this config.
     */
    public V getDefault(T t) {
        return this.defaultValue.apply(t);
    }

    /**
     * A standard getter.
     *
     * @return The {@link ConfigLevel} for this config.
     */
    public ConfigLevel getConfigLevel() {
        return this.configLevel;
    }

    /**
     * Gets the table name for this config and a config level.
     *
     * @param level the config level.
     * @return the table name for this config and a config level.
     */
    private String getNameForType(ConfigLevel level) {
        return this.name + "_" + level.name().toLowerCase();
    }

    /**
     * A standard getter.
     *
     * @return The required bot role to edit this config.
     */
    public BotRole getBotRole() {
        return this.botRole;
    }

    /**
     * If this config is fit for viewing by a user.
     *
     * @return if this config is fit for viewing by a user.
     */
    public boolean isNormalViewing() {
        return this.normalViewing;
    }

    /**
     * Gets if the config should be set after a default is gotten.
     *
     * @return if the config should be set after a default is gotten.
     */
    protected boolean setDefault() {
        return false;
    }

    /**
     * Gets the value type for this config.
     *
     * @return the value type for this config.
     */
    public Class<V> getValueType() {
        return this.valueType;
    }

    /**
     * Gets the {@link ConfigCategory} for this config.
     *
     * @return the {@link ConfigCategory} for this config.
     */
    public ConfigCategory getCategory() {
        return category;
    }

    /**
     * Gets the time the config was last set, give or
     * take for caching or -1 if the value was default.
     *
     * @param configurable the configurable to get the last set age for.
     * @return the time the config was last set, give or take for caching or -1 if the value was default.
     */
    public long getAge(Configurable configurable) {
        return Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()), set -> {
            if (!set.next()) return -1L;
            return set.getLong(3);
        });
    }

    @Deprecated
    public V wrapTypeIn(String e, T configurable) {
        return TypeChanger.toObject(this.valueType, e);
    }

    /**
     * Returns a {@link String} representation of the value.
     *
     * @param v the value to wrap out.
     * @param configurable the configurable being wrapped out.
     * @return the {@link String} representation of the value.
     */
    public String wrapTypeOut(V v, T configurable) {// configurable may be used in over ride methods
        return v instanceof Configurable ? v instanceof Channel && !(v instanceof VoiceChannel) ? ((Channel) v).mention() : ((Configurable) v).getName() : TypeChanger.toString(this.valueType, v);
    }

    /**
     * Cleans the input if possible and returns that
     * or throws and exception if the input is invalid.
     *
     * @param configurable the configurable for.
     * @param v the value to clean or throw an exception for if is invalid.
     * @return the validated input.
     */
    protected V validateInput(T configurable, V v) {return v;}

    /**
     * Sets the config value for the given configurable.
     *
     * @param configurable the configurable to set the value for.
     * @param value the value to set the config to for the given configurable.
     * @return the value set to the config.
     */
    public V setValue(T configurable, V value) {
        if (configurable == null) throw new DevelopmentException("Attempted to reset value for null configurable on config " + this.name);
        if (!this.valueType.isInstance(value) && value != null) throw new ArgumentException("Attempted passing incorrect type of argument");
        value = validateInput(configurable, value);
        if (this.cache != null) {
            if (value == null) this.nullCache.add(configurable);
            else this.cache.put(configurable, value);
        }
        return saveValue(configurable, value);
    }

    /**
     * Saves the value to the database for this config and the given configurable.
     *
     * @param configurable the configurable to save the value for.
     * @param value the value to save the config to for the given configurable.
     * @return the value saved to the database.
     */
    private V saveValue(T configurable, V value) {
        V current = this.grabValue(configurable);
        if (Objects.equals(value, current)) return value;
        V defaul = this.getDefault(configurable);
        if (Objects.equals(value, defaul)) reset(configurable);
        else if (Objects.equals(current, defaul)) Database.insert("INSERT INTO " + this.getNameForType(configurable.getConfigLevel()) + " (`id`, `value`, `millis`) VALUES ('" + configurable.getID() + "','" + this.getSQLRepresentation(value) + "','" + System.currentTimeMillis() + "');");
        else Database.insert("UPDATE " + this.getNameForType(configurable.getConfigLevel()) + " SET millis = " + System.currentTimeMillis() + ", value = " + Database.quote(this.getSQLRepresentation(value)) + " WHERE id = " + Database.quote(configurable.getID()) + ";");
        return value;
    }

    /**
     * Resets the value of the config for the given configurable.
     *
     * @param configurable the configurable to reset the config value for.
     */
    public void reset(T configurable) {
        if (configurable == null) throw new DevelopmentException("Attempted to reset value for null configurable on config " + this.name);
        if (this.cache != null) {
            this.nullCache.remove(configurable);
            this.cache.remove(configurable);
        }
        Database.query("DELETE FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()));
    }

    /**
     * Gets the value for the given {@link Configurable}.
     *
     * @param configurable the configurable that the setting is being gotten for.
     * @return the config's value for the given {@link Configurable}.
     */
    public V getValue(T configurable) {
        if (configurable == null) throw new DevelopmentException("Attempted to get value for null configurable on config " + this.name);
        if (this.cache == null) return grabValue(configurable);
        V value = this.cache.get(configurable);
        if (value == null && !this.nullCache.contains(configurable)) {
            value = grabValue(configurable);
            if (value == null) this.nullCache.add(configurable);
            else this.cache.put(configurable, value);
        }
        return value;
    }

    /**
     * Grabs the value for this config from the {@link Database} for the given {@link Configurable}.
     *
     * @param configurable the {@link Configurable} that the setting is being gotten for.
     * @return the config's value for the given {@link Configurable}.
     */
    private V grabValue(T configurable) {
        try {
            return Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()), set -> {
                try{set.next();
                    return parseValue(set);
                } catch (SQLException e) {
                    if (!e.getMessage().equals("Current position is after the last row")) Log.log("Exception while getting value", e);
                    return this.getDefault(configurable);
                }
            });
        } catch (StreamException e) {
            throw new DevelopmentException("Could not load config due to stream exception from " + this.getNameForType(configurable.getConfigLevel()) + " while loading " + configurable.getID() + "'s value", e);
        }
    }

    private V parseValue(ResultSet set) throws SQLException {
        if (Configurable.class.isAssignableFrom(this.valueType)) return (V) ConfigHandler.getConfigurable((Class<? extends Configurable>) this.valueType, set.getString(2));
        if (this.valueType.equals(Long.class)) return (V) Long.valueOf(set.getLong(2));
        if (this.valueType.equals(Boolean.class)) return (V) Boolean.valueOf(set.getBoolean(2));
        if (this.valueType.equals(Float.class)) return (V) Float.valueOf(set.getFloat(2));
        if (this.valueType.equals(Integer.class)) return (V) Integer.valueOf(set.getInt(2));
        return TypeChanger.toObject(this.valueType, set.getString(2));
    }

    /**
     * Uses a function to set the value of the config to a new value.
     *
     * @param configurable the configurable the config is to be set for.
     * @param function the function the config gives the old value to and gets a new value from.
     */
    public V changeSetting(T configurable, Function<V, V> function) {
        return this.setValue(configurable, function.apply(this.getValue(configurable)));
    }

    /**
     * Used to alter the value for configs which's value is a object.
     *
     * @param configurable the configurable for which the value is being saved.
     * @param consumer consumer to alter the current value to then save.
     * @return the value saved to the database.
     */
    public V alterSetting(T configurable, Consumer<V> consumer) {
        V val = ObjectCloner.clone(this.getValue(configurable));
        consumer.accept(val);
        return this.setValue(configurable, val);
    }

    /**
     * Sets the value of a config for a given configurable if the value is a default.
     *
     * @param configurable the configurable to dry a config for.
     * @param function the function to determine a config for.
     * @return the value set for the given configurable.
     */
    public V setIfDefault(T configurable, Function<V, V> function) {
        V value = getValue(configurable);
        if (Objects.equals(value, this.getDefault(configurable))) value = this.setValue(configurable, function.apply(value));
        return value;
    }

    /**
     * Computes and sets the config if the value is old.
     *
     * @param configurable the configurable to set the value for.
     * @param age the age a config must be to change the value for the given configurable.
     * @param function the function to compute and set to the result for.
     * @return the value set or kept from the.
     */
    public V setIfOld(T configurable, long age, Function<V, V> function) {
        V val = this.getValue(configurable);
        if (System.currentTimeMillis() - getAge(configurable) >= age) val = function.apply(val);
        return val;
    }

    /**
     * Gets the string representation for a config value.
     *
     * @param configurable the configurable to get the representation for.
     * @return gets the {@link String} representation
     * of the config value for the given configurable.
     */
    public String getExteriorValue(T configurable) {
        String result = wrapTypeOut(getValue(configurable), configurable);
        if (result.equals("null")) result = getValueType().getSimpleName() + " not set";
        return result;
    }

    /**
     * Sets the value of a config for the given configurable based on a string conversion.
     *
     * @param configurable the configurable to set the config value for.
     * @param user the user in the context.
     * @param channel the channel in the context.
     * @param guild the guild in the context.
     * @param message the message in the context.
     * @param value the string value to derive the configurable's new value from.
     */
    public void setExteriorValue(T configurable, User user, Channel channel, Guild guild, Message message, String value) {
        if (!this.isNormalViewing()) throw new ArgumentException("Slow down there malicious user, we have that covered!");
        if (value.length() == 7 && value.toLowerCase().equals("not set")) value = "null";
        setValue(configurable, InvocationObjectGetter.convert(this.getValueType(), user, null, channel, guild, message, null, value).getKey());
    }

    /**
     * The default version of {@link AbstractConfig#getNonDefaultSettings(Class)}
     * where the argument is {@link AbstractConfig#getConfigLevel()#getValueType()}.
     *
     * @return a map representation of {@link Configurable}s and values for this config.
     */
    public Map<T, V> getNonDefaultSettings() {
        return this.getNonDefaultSettings(this.getConfigLevel().getType());
    }

    /**
     * Gets a map representation if {@link Configurable}s
     * and their values if the values are not default.
     *
     * @param clazz the class type to get configurables and values for.
     * @return a map representation of {@link Configurable}s and values for this config.
     */
    public Map<T, V> getNonDefaultSettings(Class<? extends Configurable> clazz) {
        return Database.select("SELECT * FROM " + this.getNameForType(ConfigLevel.getLevel(clazz)), set -> {
            Map<T, V> map = new HashMap<>(set.getFetchSize());
            V val;
            Configurable c;
            while (set.next()) {
                c = ConfigHandler.getConfigurable(set.getString(1));
                if (c == null) continue;// skip if it no longer exists within the scope of the bot
                T t;
                try {
                    t = (T) c.convert(clazz);
                } catch (ArgumentException e) {
                    Log.log("While getting defaults a conversion to type " + clazz.getSimpleName() + " for " + c.getID() + " could not be found");
                    continue;
                }
                if (this.cache != null) {
                    if (this.nullCache.contains(t)) map.put(t, null);
                    else {
                        try {
                            val = parseValue(set);
                            if (val != null) this.cache.put(t, val);
                            else this.nullCache.add(t);
                            map.put(t, val);
                        } catch (SQLException e) {
                            Log.log("Exception getting non-default setting values for " + this.name + " for configurable " + t.getID(), e);
                        }
                    }
                } else try {
                    map.put(t, TypeChanger.toObject(this.valueType, set.getString(2)));
                } catch (SQLException e) {
                    Log.log("Exception getting non-default setting values for " + this.name + " for configurable " + t.getID(), e);
                }
            }
            return map;
        });
    }

    private String getSQLRepresentation(V value) {
        if (this.valueType.equals(Boolean.class)) return (Boolean) value ? "1" : "0";
        return TypeChanger.toString(this.valueType, value);
    }

    private String getSQLTableType() {
        if (this.valueType.equals(Long.class)) return "BIGINT";
        if (this.valueType.equals(Boolean.class)) return "BOOLEAN";
        if (this.valueType.equals(Float.class)) return "FLOAT";
        if (this.valueType.equals(Integer.class)) return "INTEGER";
        if (this.valueType.isEnum()) return "TINYTEXT";
        if (Configurable.class.isAssignableFrom(this.valueType)) return ConfigLevel.getLevel((Class<? extends Configurable>) this.valueType).isLongID() ? "BIGINT" : "TINYTEXT";
        return "TEXT";
    }
}
