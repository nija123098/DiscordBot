package com.github.kaaz.discordbot.config;

/**
 * The enum to represent a type of configurable object.
 * Objects that can be configured must have a type.
 *
 * @author nija123098
 * @since 2.0.0
 */
public enum ConfigLevel {
    /** The type for a user's config */
    USER,
    /** The type for a channel's config */
    CHANNEL,
    /** The type for a user's config within a guild*/
    GUILD_USER,
    /** The type for a guild's config */
    GUILD,
    /** The type for global config */
    GLOBAL,;
}
