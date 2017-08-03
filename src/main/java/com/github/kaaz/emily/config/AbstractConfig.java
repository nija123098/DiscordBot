package com.github.kaaz.emily.config;

import com.github.kaaz.emily.command.annotations.LaymanName;
import com.github.kaaz.emily.db.Database;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.perms.BotRole;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private final Class<V> valueType;
    private final boolean normalViewing;
    public AbstractConfig(String name, BotRole botRole, V defaul, String description) {
        this(name, botRole, description, v -> defaul);
    }
    public AbstractConfig(String name, BotRole botRole, String description, Function<T, V> defaul) {
        this.name = name;
        this.botRole = botRole;
        this.defaul = defaul;
        this.description = description;
        Type[] types = TypeChanger.getRawClasses(this.getClass());
        this.valueType = (Class<V>) types[0];
        if (!ObjectCloner.supports(this.valueType)) throw new DevelopmentException("Cloner does not support type: " + this.valueType.getName());
        this.normalViewing = TypeChanger.normalStorage(this.valueType);
        this.configLevel = ConfigLevel.getLevel((Class<T>) types[1]);
        EventDistributor.register(this);
        this.configLevel.getAssignable().forEach(level -> {
            try (ResultSet rs = Database.getConnection().getMetaData().getTables(null, null, this.getNameForType(level), null)) {
                while (rs.next()) {
                    String tName = rs.getString("TABLE_NAME");
                    if (tName != null && tName.equals(this.getNameForType(level))) return;
                }
                Database.query("CREATE TABLE `" + this.getNameForType(level) + "` (id VARCHAR(150), value VARCHAR(" + (this.normalViewing ? 100 : 1000) + "), millis BIGINT)");
            } catch (SQLException e) {
                throw new DevelopmentException("Could not ensure table existence", e);
            }
        });
    }

    protected void onLoad(){}

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

    private String getNameForType(ConfigLevel level){
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

    public long getAge(Configurable configurable){
        try{ResultSet set = Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()));
            if (!set.next()) return -1;
            set.getLong(3);
        } catch (SQLException e) {
            e.printStackTrace();// check
        }
        return -1;
    }

    public V wrapTypeIn(String e, T configurable){
        return TypeChanger.toObject(this.valueType, e);
    }
    public String wrapTypeOut(V v, T configurable){// configurable may be used in over ride methods
        return TypeChanger.toString(this.valueType, v);
    }
    protected void validateInput(T configurable, V v) {}
    public V setValue(T configurable, V value){
        validateInput(configurable, value);
        if (Objects.equals(value, this.getDefault(configurable))) Database.query("DELETE FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()));
        else {
            Database.query("DELETE FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()));
            Database.insert("INSERT INTO " + this.getNameForType(configurable.getConfigLevel()) + " (`id`, `value`, `millis`) VALUES ('" + configurable.getID() + "','" + TypeChanger.toString(this.valueType, value) + "','" + System.currentTimeMillis() + "');");
        }
        return value;
    }

    /**
     * Gets the value for the given value.
     *
     * @param configurable the configurable that the
     *                     setting is being gotten for
     * @return the config's value
     */
    public V getValue(T configurable){// slq here as well
        ResultSet resultSet = Database.select("SELECT * FROM " + this.getNameForType(configurable.getConfigLevel()) + " WHERE id = " + Database.quote(configurable.getID()));
        try{resultSet.next();
            return TypeChanger.toObject(this.valueType, resultSet.getString(2));
        } catch (SQLException e) {
            return this.getDefault(configurable);
        }
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
        return wrapTypeOut(getValue(configurable), configurable);
    }

    public void setExteriorValue(T configurable, String value) {
        setValue(configurable, wrapTypeIn(value, configurable));
    }

    public Map<T, V> getNonDefaultSettings(){// SQL
        return new HashMap<>();
    }
}
