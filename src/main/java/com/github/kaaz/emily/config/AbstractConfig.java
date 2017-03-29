package com.github.kaaz.emily.config;

import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.TypeTranslator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author nija123098
 * @since 2.0.0
 * @param <I> The stored type of the config within the database
 * @param <E> The external value of a config before processing it to the stored type
 * @param <T> The type of config that this config defines
 *
 */
public class AbstractConfig<I, E, T extends Configurable> {// interior, exterior, type
    private I defaul;
    private String name, description;
    private BotRole botRole;
    private ConfigLevel configLevel;
    private Class<I> internalType;
    private Class<E> exteriorType;
    private Class<T> configurableType;
    public AbstractConfig(String name, BotRole botRole, I defaul, String description) {
        this.name = name;
        this.botRole = botRole;
        this.defaul = defaul;
        this.description = description;
        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        this.internalType = (Class<I>) types[0];
        this.exteriorType = (Class<E>) types[1];
        this.configurableType = (Class<T>) types[2];
        this.configLevel = ConfigLevel.getLevel(this.configurableType);
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
    public I getDefault(){
        return this.defaul;
    }

    /**
     * A setter for the init stage.
     *
     * @param level the level for the config to be set at
     */
    void setConfigLevel(ConfigLevel level){
        this.configLevel = level;
    }

    /**
     * A standard getter.
     *
     * @return The config level for this config
     */
    public ConfigLevel getConfigLevel(){
        return this.configLevel;
    }
    public I wrapTypeIn(E e, T configurable){
        return TypeTranslator.translate(e, getValue(configurable));
    }
    public E wrapTypeOut(I i, T configurable){// configurable may be used in over ride methods
        return TypeTranslator.translate(i, this.exteriorType);
    }
    void setExteriorValue(T configurable, E value){
        setValue(configurable, wrapTypeIn(value, configurable));
    }
    // TODO SQL stuff goes here, more or less
    void setValue(T configurable, I value){

    }

    /**
     * Gets the value for the given value.
     *
     * @param configurable the configurable that the
     *                     setting is being gotten for
     * @return the config's value
     */
    I getValue(T configurable){// slq here as well
        return null;
    }
    E getExteriorValue(T configurable){
        return wrapTypeOut(getValue(configurable), configurable);
    }
}
