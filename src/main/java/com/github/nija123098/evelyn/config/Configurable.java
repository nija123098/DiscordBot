package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.config.configs.ConfigurableExistsConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;

/**
 * A helper class that all objects that can
 * have configuration values may implement.
 * Such object types are indicated by ConfigLevel
 *
 * @author nija123098
 * @since 2.0.0
 * @see ConfigLevel
 */
public interface Configurable {
    /**
     * Gets the snowflake for this object
     *
     * @return the snowflake
     */
    String getID();

    String getName();

    /**
     * Gets the config level of this object
     *
     * @return the config level of this object
     */
    ConfigLevel getConfigLevel();

    /**
     * The method to manage a configurable, called periodically
     */
    default void manage(){}

    /**
     * Checks the permission to edit a config,
     * throws an exception if it can not edit.
     *
     * This is only the first implementation
     * of config permission checks, config
     * specific permission checks are used after this.
     *
     * @param user the user attempting to edit a config
     * @param guild the guild in which the config is trying to be edited
     */
    void checkPermissionToEdit(User user, Guild guild);

    default Configurable getGoverningObject(){
        return GlobalConfigurable.GLOBAL;
    }

    default boolean shouldCache(){
        return false;
    }

    default <T extends Configurable> Configurable convert(Class<T> t){
        if (t.equals(this.getClass())) return this;
        throw new ArgumentException("This configurable can not be morphed into that type of configurable: " + t.getName());
    }

    default void registerExistence(){
        Database.bufferCall(() -> ConfigHandler.setSetting(ConfigurableExistsConfig.class, this, true));
    }
}
