package com.github.kaaz.emily.config;

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
}
