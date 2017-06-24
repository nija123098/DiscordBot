package com.github.kaaz.emily.config;

import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.exeption.DevelopmentException;
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
@LaymanName(value = "Configuration name", help = "The config name")
public class AbstractConfig<V, T extends Configurable> {
    private Map<T, V> noSaveValues = new HashMap<>();
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

    public V wrapTypeIn(String e, T configurable){
        return TypeChanger.toObject(this.valueType, e);
    }
    public String wrapTypeOut(V v, T configurable){// configurable may be used in over ride methods
        return TypeChanger.toString(this.valueType, v);
        //return OTypeTranslator.translate(v, String.class);
    }
    protected void validateInput(T configurable, V v) {}
    // TODO SQL stuff goes here, more or less
    public V setValue(T configurable, V value){
        validateInput(configurable, value);
        EventDistributor.distribute(new ConfigValueChangeEvent(configurable, this, this.getValue(configurable), value));
        map.put(configurable, TypeChanger.toString(this.valueType, value));
        return value;
    }
    private Map<Configurable, String> map = new HashMap<>();//TODO REMOVE TESTING

    /**
     * Gets the value for the given value.
     *
     * @param configurable the configurable that the
     *                     setting is being gotten for
     * @return the config's value
     */
    public V getValue(T configurable){// slq here as well
        return map.containsKey(configurable) ? TypeChanger.toObject(this.valueType, map.get(configurable)) : this.getDefault(configurable);
        //return (V) map.computeIfAbsent(configurable, c -> this.getDefault());
    }

    /**
     * Uses a function to set the value of the config to a new value.
     *
     * @param configurable the configurable the config is to be set for
     * @param function the function the config gives the old value to and gets a new value from
     */
    public V changeSetting(T configurable, Function<V, V> function){
        return this.setValue(configurable, function.apply(this.getValue(configurable)));
    }

    public V alterSetting(T configurable, Consumer<V> consumer){
        V val = ObjectCloner.clone(this.getValue(configurable));
        consumer.accept(val);
        return this.setValue(configurable, val);
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
