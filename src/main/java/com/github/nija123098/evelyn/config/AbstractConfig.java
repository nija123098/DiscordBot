package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.Log;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author nija123098
 * @since 2.0.0
 * @param <V> The stored type of the config within the database
 * @param <T> The type of config that this config defines
 */
@LaymanName(value = "Configuration name", help = "The config name")
public class AbstractConfig<V, T extends Configurable> {
    private final Function<T, V> defaul;
    private final String name, description;
    private final BotRole botRole;
    private final ConfigLevel configLevel;
    private final ConfigCategory catagory;
    private final Class<V> valueType;
    private final boolean normalViewing;
    private final Map<T, V> cache;
    public AbstractConfig(String name, ConfigCategory category, V defaul, String description) {
        this(name, category, v -> defaul, description);
    }
    public AbstractConfig(String name, BotRole botRole, ConfigCategory category, V defaul, String description) {
        this(name, botRole, category, v -> defaul, description);
    }
    public AbstractConfig(String name, ConfigCategory category, Function<T, V> defaul, String description) {
        this(name, category.getBotRole(), category, defaul, description);
    }
    public AbstractConfig(String name, BotRole botRole, ConfigCategory category, Function<T, V> defaul, String description) {
        this.name = name;
        this.botRole = botRole;
        this.defaul = defaul;
        this.description = description;
        this.catagory = category;
        this.catagory.addConfig(this);
        Type[] types = TypeChanger.getRawClasses(this.getClass());
        this.valueType = types.length == 1 ? (Class<V>) Integer.class : (Class<V>) types[0];
        if (!ObjectCloner.supports(this.valueType)) throw new DevelopmentException("Cloner does not support type: " + this.valueType.getName());
        this.normalViewing = TypeChanger.normalStorage(this.valueType);
        this.configLevel = ConfigLevel.getLevel((Class<T>) types[types.length - 1]);
        if (this.configLevel.mayCache()){
            this.cache = new ConcurrentHashMap<>();
            Launcher.registerShutdown(this::saveCashed);
            ScheduleService.scheduleRepeat(600_000, 600_000, this::saveCashed);
        }else this.cache = null;
        EventDistributor.register(this);
        this.configLevel.getAssignable().forEach(level -> {
            if (level == ConfigLevel.ALL) return;
            try (ResultSet rs = Database.getConnection().getMetaData().getTables(null, null, this.getNameForType(level), null)) {
                while (rs.next()) {
                    String tName = rs.getString("TABLE_NAME");
                    if (tName != null && tName.equals(this.getNameForType(level))) return;
                }// make
                Database.query("CREATE TABLE `" + this.getNameForType(level) + "` (id TINYTEXT, value TEXT, millis BIGINT)");
            } catch (SQLException e) {
                throw new DevelopmentException("Could not ensure table existence", e);
            }
        });
    }

    private void saveCashed(){// make slowly change, not all at once unless shutting down
        this.cache.forEach((t, integer) -> {
            Care.lessSleep(10);
            this.saveValue(t, this.cache.remove(t));
        });
    }

    /**
     * A standard getter
     *
     * @return the name of the config
     */
    public String getName() {
        return this.name;
    }

    /**
     * A standard getter
     *
     * @return the multi-line config description
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
     * A standard getter
     *
     * @return the default value of this config
     */
    public V getDefault(T t){
        return this.defaul.apply(t);
    }

    /**
     * A standard getter.
     *
     * @return The config level for this config
     */
    public ConfigLevel getConfigLevel(){
        return this.configLevel;
    }

    public String getNameForType(ConfigLevel level){
        return this.name + "_" + level.name().toLowerCase();
    }

    /**
     * A standard getter.
     *
     * @return The required bot role to edit this config.
     */
    public BotRole getBotRole(){
        return this.botRole;
    }

    public boolean isNormalViewing() {
        return this.normalViewing;
    }

    public boolean checkDefault(){
        return true;
    }

    public Class<V> getValueType(){
        return this.valueType;
    }

    public long getAge(Configurable configurable){
        return Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()), set -> {
            if (!set.next()) return -1L;
            return set.getLong(3);
        });
    }

    public V wrapTypeIn(String e, T configurable){
        return TypeChanger.toObject(this.valueType, e);
    }
    public String wrapTypeOut(V v, T configurable){// configurable may be used in over ride methods
        return v instanceof Configurable ? v instanceof Channel && !(v instanceof VoiceChannel) ? ((Channel) v).mention() : ((Configurable) v).getName() : TypeChanger.toString(this.valueType, v);
    }
    protected V validateInput(T configurable, V v) {return v;}
    public V setValue(T configurable, V value){
        if (!(value == null || this.valueType.isInstance(value))) throw new ArgumentException("Attempted passing incorrect type of argument");
        value = validateInput(configurable, value);
        if (this.cache == null || value == null) saveValue(configurable, value);
        else this.cache.put(configurable, value);
        return value;
    }
    private V saveValue(T configurable, V value){
        reset(configurable);
        if (!(this.checkDefault() && Objects.equals(value, this.getDefault(configurable)))) {
            Database.insert("INSERT INTO " + this.getNameForType(configurable.getConfigLevel()) + " (`id`, `value`, `millis`) VALUES ('" + configurable.getID() + "','" + TypeChanger.toString(this.valueType, value) + "','" + System.currentTimeMillis() + "');");
        }
        return value;
    }
    public void reset(T configurable){
        if (this.cache != null) this.cache.remove(configurable);
        Database.query("DELETE FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()));
    }

    /**
     * Gets the value for the given value.
     *
     * @param configurable the configurable that the
     *                     setting is being gotten for
     * @return the config's value
     */
    public V getValue(T configurable){// slq here as well
        V value;
        if (this.cache == null || (value = this.cache.get(configurable)) == null) value = grabValue(configurable);
        if (this.cache != null && value != null) return this.cache.computeIfAbsent(configurable, this::grabValue);
        return value;
    }
    private V grabValue(T configurable){
        return Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()), set -> {
            try{set.next();
                return TypeChanger.toObject(this.valueType, set.getString(2));
            } catch (SQLException e) {
                if (!e.getMessage().equals("Current position is after the last row")) Log.log("Error while getting value", e);
                return this.getDefault(configurable);
            }
        });
    }

    /**
     * Uses a function to set the value of the config to a new value.
     *
     * @param configurable the configurable the config is to be set for
     * @param function the function the config gives the old value to and gets a new value from
     */
    public V changeSetting(T configurable, Function<V, V> function) {
        return this.setValue(configurable, function.apply(this.getValue(configurable)));
    }

    public V alterSetting(T configurable, Consumer<V> consumer) {
        V val = ObjectCloner.clone(this.getValue(configurable));
        consumer.accept(val);
        return this.setValue(configurable, val);
    }

    public V setIfDefault(T configurable, Function<V, V> function) {
        V value = getValue(configurable);
        if (Objects.equals(value, this.getDefault(configurable))) value = this.setValue(configurable, function.apply(value));
        return value;
    }

    public V setIfOld(T configurable, long age, Function<V, V> function) {
        V val = this.getValue(configurable);
        if (System.currentTimeMillis() - getAge(configurable) >= age) val = function.apply(val);
        return val;
    }

    public String getExteriorValue(T configurable) {
        String result = wrapTypeOut(getValue(configurable), configurable);
        if (result.equals("null")) result = "not set";
        return result;
    }

    public void setExteriorValue(T configurable, User user, Channel channel, Guild guild, Message message, String value) {
        if (!this.isNormalViewing()) throw new ArgumentException("Slow down there malicious user, we have that covered!");
        if (value.length() == 7 && value.toLowerCase().equals("not set")) value = "null";
        setValue(configurable, InvocationObjectGetter.convert(this.getValueType(), user, null, channel, guild, message, null, value).getKey());
    }

    public Map<T, V> getNonDefaultSettings() {
        return getNonDefaultSettings(this.getConfigLevel().getType());
    }

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
}
