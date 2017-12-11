package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.Care;
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
public class AbstractConfig<V, T extends Configurable> {
    private final Function<T, V> defaul;
    private final String tableName, name, description;
    private final BotRole botRole;
    private final ConfigLevel configLevel;
    private final ConfigCategory category;
    private final Class<V> valueType;
    private final boolean normalViewing;
    private final Map<T, V> cache;
    private final Set<T> change;
    public AbstractConfig(String tableName, String name, ConfigCategory category, V defaul, String description) {
        this(tableName, name, category, v -> defaul, description);
    }
    public AbstractConfig(String tableName, String name, BotRole botRole, ConfigCategory category, V defaul, String description) {
        this(tableName, name, botRole, category, v -> defaul, description);
    }
    public AbstractConfig(String tableName, String name, ConfigCategory category, Function<T, V> defaul, String description) {
        this(tableName, name, category.getBotRole(), category, defaul, description);
    }

    /**
     * The constructor to make a config instance.
     *
     * @param tableName the tableName of the config, spaces are not allowed.
     * @param name the display name of the config, required
     * @param botRole the minimum role allowed to change the value.
     * @param category the catagory to that the config is in.
     * @param defaul the function to get the default value for a given config.
     * @param description a description of the config.
     */
    public AbstractConfig(String tableName, String name, BotRole botRole, ConfigCategory category, Function<T, V> defaul, String description) {
        this.tableName = tableName;
        this.name = name;
        this.botRole = botRole;
        this.defaul = defaul;
        this.description = description;
        this.category = category;
        this.category.addConfig((AbstractConfig<? extends Configurable, ?>) this);
        Type[] types = TypeChanger.getRawClasses(this.getClass());
        this.valueType = (Class<V>) types[0];
        if (!ObjectCloner.supports(this.valueType)) throw new DevelopmentException("Cloner does not support type: " + this.valueType.getName());
        this.normalViewing = TypeChanger.normalStorage(this.valueType);
        this.configLevel = ConfigLevel.getLevel((Class<T>) types[types.length - 1]);
        if (this.configLevel.mayCache()){
            this.cache = new ConcurrentHashMap<>();
            this.change = new HashSet<>();
            ScheduleService.scheduleRepeat(600_000, 600_000, this::saveCashed);
        }else {
            this.cache = null;
            this.change = null;
        }
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
     * Saves any cached values and removes them from the cache.
     */
    void saveCashed(){// make slowly change, not all at once unless shutting down
        this.cache.forEach((t, val) -> {
            V v = this.cache.remove(t);
            if (v == null || !this.change.remove(t)) return;
            this.saveValue(t, v);
            Care.lessSleep(10);
        });
    }

    /**
     * A standard getter.
     *
     * @return the name of the config.
     */
    public String getName() {
        return this.tableName;
    }

    /**
     * A standard getter.
     *
     * @return the display name of the config
     */
    public String getDisplayName() {
        return this.name;
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
    public V getDefault(T t){
        return this.defaul.apply(t);
    }

    /**
     * A standard getter.
     *
     * @return The {@link ConfigLevel} for this config.
     */
    public ConfigLevel getConfigLevel(){
        return this.configLevel;
    }

    /**
     * Gets the table name for this config and a config level.
     *
     * @param level the config level.
     * @return the table name for this config and a config level.
     */
    private String getNameForType(ConfigLevel level){
        return this.tableName + "_" + level.name().toLowerCase();
    }

    /**
     * A standard getter.
     *
     * @return The required bot role to edit this config.
     */
    public BotRole getBotRole(){
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
    protected boolean setDefault(){
        return false;
    }

    /**
     * Gets the value type for this config.
     *
     * @return the value type for this config.
     */
    public Class<V> getValueType(){
        return this.valueType;
    }

    /**
     * Gets the time the config was last set, give or
     * take for caching or -1 if the value was default.
     *
     * @param configurable the configurable to get the last set age for.
     * @return the time the config was last set, give or take for caching or -1 if the value was default.
     */
    public long getAge(Configurable configurable){
        return Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()), set -> {
            if (!set.next()) return -1L;
            return set.getLong(3);
        });
    }

    @Deprecated
    public V wrapTypeIn(String e, T configurable){
        return TypeChanger.toObject(this.valueType, e);
    }

    /**
     * Returns a {@link String} representation of the value.
     *
     * @param v the value to wrap out.
     * @param configurable the configurable being wrapped out.
     * @return the {@link String} representation of the value.
     */
    public String wrapTypeOut(V v, T configurable){// configurable may be used in over ride methods
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
    public V setValue(T configurable, V value, boolean overrideCache){
        if (!(value == null || this.valueType.isInstance(value))) throw new ArgumentException("Attempted passing incorrect type of argument");
        value = validateInput(configurable, value);
        if (overrideCache || this.cache == null || value == null) return saveValue(configurable, value);
        else {
            this.cache.put(configurable, value);
            this.change.add(configurable);
        }
        return value;
    }

    /**
     * Saves the value to the database for this config and the given configurable.
     *
     * @param configurable the configurable to save the value for.
     * @param value the value to save the config to for the given configurable.
     * @return the value saved to the database.
     */
    private V saveValue(T configurable, V value){
        V current = this.getValue(configurable), defaul = this.getDefault(configurable);
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
    public void reset(T configurable){
        if (this.cache != null) {
            this.change.remove(configurable);
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
    public V getValue(T configurable){// slq here as well
        V value;
        if (this.cache == null) value = grabValue(configurable);
        else{
            value = this.cache.get(configurable);
            if (value == null) value = grabValue(configurable);
        }
        if (this.cache != null && value != null) return this.cache.computeIfAbsent(configurable, this::grabValue);
        return value;
    }

    /**
     * Grabs the value for this config from the {@link Database} for the given {@link Configurable}.
     *
     * @param configurable the {@link Configurable} that the setting is being gotten for.
     * @return the config's value for the given {@link Configurable}.
     */
    private V grabValue(T configurable){
        try {
            return Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()), set -> {
                try{set.next();
                    return TypeChanger.toObject(this.valueType, set.getString(2));
                } catch (SQLException e) {
                    if (!e.getMessage().equals("Current position is after the last row")) Log.log("Error while getting value", e);
                    return this.getDefault(configurable);
                }
            });
        } catch (StreamException e){
            throw new DevelopmentException("Could not load config due to stream exception from " + this.getNameForType(configurable.getConfigLevel()) + " while loading " + configurable.getID() + "'s value", e);
        }
    }

    /**
     * Uses a function to set the value of the config to a new value.
     *
     * @param configurable the configurable the config is to be set for.
     * @param function the function the config gives the old value to and gets a new value from.
     */
    public V changeSetting(T configurable, Function<V, V> function) {
        return this.setValue(configurable, function.apply(this.getValue(configurable)), false);
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
        return this.setValue(configurable, val, false);
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
        if (Objects.equals(value, this.getDefault(configurable))) value = this.setValue(configurable, function.apply(value), false);
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
        setValue(configurable, InvocationObjectGetter.convert(this.getValueType(), user, null, channel, guild, message, null, value).getKey(), true);
    }

    /**
     * The default version of {@link AbstractConfig#getNonDefaultSettings(Class)}
     * where the argument is {@link AbstractConfig#getConfigLevel()#getValueType()}.
     *
     * @return a map representation of {@link Configurable}s and values for this config.
     */
    public Map<T, V> getNonDefaultSettings() {
        return getNonDefaultSettings(this.getConfigLevel().getType());
    }

    /**
     * Gets a map representation if {@link Configurable}s
     * and their values if the values are not default.
     *
     * @param clazz the class type to get configurables and values for.
     * @return a map representation of {@link Configurable}s and values for this config.
     */
    public Map<T, V> getNonDefaultSettings(Class<? extends Configurable> clazz){
        return Database.select("SELECT * FROM " + this.getNameForType(ConfigLevel.getLevel(clazz)), set -> {
            Map<T, V> map = new HashMap<>();
            try {
                while (set.next()){
                    T t = (T) ConfigHandler.getConfigurable(set.getString(1));
                    if (t == null) continue;
                    map.put(t, TypeChanger.toObject(this.valueType, set.getString(2)));
                }
            } catch (SQLException e) {
                Log.log("Exception getting non-default setting values, got " + map.size(), e);
            }
            return map;// used for foreach, needn't be optimized
        });
    }

    private String getSQLRepresentation(V value){
        if (this.valueType.equals(Boolean.class)) return (Boolean) value ? "1" : "0";
        return TypeChanger.toString(this.valueType, value);
    }

    private String getSQLTableType(){
        if (this.valueType.equals(Long.class)) return "LONG";
        if (this.valueType.equals(Boolean.class)) return "BOOLEAN";
        if (this.valueType.equals(Float.class)) return "FLOAT";
        if (this.valueType.equals(Integer.class)) return "INTEGER";
        if (this.valueType.isEnum()) return "TINYTEXT";
        if (Configurable.class.isAssignableFrom(this.valueType)) return ConfigLevel.getLevel((Class<? extends Configurable>) this.valueType).isLongID() ? "LONG" : "TINYTEXT";
        return "TEXT";
    }
}
