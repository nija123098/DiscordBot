package com.github.kaaz.emily.config;

import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.perms.BotRole;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author nija123098
 * @since 2.0.0
 * @param <V> The stored type of the config within the database
 * @param <T> The type of config that this config defines
 */
@LaymanName(value = "Configuration type then name", help = "The type (user, guild, guild user, ect...) followed by the config name of the playlist")
public class AbstractConfig<V, T extends Configurable> {
    private final V defaul;
    private final String name, description;
    private final BotRole botRole;
    private final ConfigLevel configLevel;
    private final Class<V> valueType;
    public AbstractConfig(String name, BotRole botRole, V defaul, String description) {
        this.name = name;
        this.botRole = botRole;
        this.defaul = defaul;
        this.description = description;
        Type[] types = TypeTranslator.getRawClasses(this.getClass());
        this.valueType = (Class<V>) types[0];
        this.configLevel = ConfigLevel.getLevel((Class<T>) types[1]);
        EventDistributor.register(this);
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
    public V getDefault(){
        return this.defaul;
    }

    /**
     * A standard getter.
     *
     * @return The config level for this config
     */
    public ConfigLevel getConfigLevel(){
        return this.configLevel;
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
     * Gets if the config should be saved across sessions
     *
     * @return if this value should be saved across sessions
     */
    public boolean shouldSave(){
        return true;
    }
    public V wrapTypeIn(String e, T configurable){
        return TypeTranslator.toType(e, this.valueType, getValue(configurable));
    }
    public String wrapTypeOut(V v, T configurable){// configurable may be used in over ride methods
        return TypeTranslator.toString(v, this.valueType, null);
        //return OTypeTranslator.translate(v, String.class);
    }
    protected void validateInput(T configurable, V v) {}
    // TODO SQL stuff goes here, more or less
    public void setValue(T configurable, V value){
        validateInput(configurable, value);
        EventDistributor.distribute(new ConfigValueChangeEvent(configurable, this, this.getValue(configurable), value));
        map.put(configurable, value);
    }
    private Map<Configurable, Object> map = new HashMap<>();//TODO REMOVE TESTING

    /**
     * Gets the value for the given value.
     *
     * @param configurable the configurable that the
     *                     setting is being gotten for
     * @return the config's value
     */
    public V getValue(T configurable){// slq here as well
        return (V) map.computeIfAbsent(configurable, c -> this.getDefault());
    }

    /**
     * Uses a function to set the value of the config to a new value.
     *
     * @param configurable the configurable the config is to be set for
     * @param function the function the config gives the old value to and gets a new value from
     */
    public void changeSetting(T configurable, Function<V, V> function){
        this.setValue(configurable, function.apply(this.getValue(configurable)));
    }

    public void changeSetting(T configurable, Consumer<V> consumer){
        V val = this.getValue(configurable);
        consumer.accept(val);
        this.setValue(configurable, val);
    }

    public String getExteriorValue(T configurable){
        return wrapTypeOut(getValue(configurable), configurable);
    }

    public void setExteriorValue(T configurable, String value){
        setValue(configurable, wrapTypeIn(value, configurable));
    }

    public Map<T, V> getNonDefaultSettings(){// SQL
        return new HashMap<>();
    }
}
