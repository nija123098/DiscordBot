package com.github.kaaz.emily.config;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

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
}
