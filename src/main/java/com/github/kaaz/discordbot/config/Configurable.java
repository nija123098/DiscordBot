package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.discordobjects.wrappers.Guild;
import com.github.kaaz.discordbot.discordobjects.wrappers.User;
import com.github.kaaz.discordbot.util.Holder;

import java.util.HashMap;

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
    default <E> void setSetting(Class<? extends AbstractConfig<E>> clazz, E o){
        ConfigHandler.setSetting(clazz, this, o);
    }
    default void setSetting(String configName, Object o){
        ConfigHandler.setSetting(configName, this, o);
    }
    default <E> E getSetting(Class<? extends AbstractConfig<E>> clazz){
        return ConfigHandler.getSetting(clazz, this);
    }
    @SuppressWarnings("unchecked")
    default <E> E getSetting(String configName, Holder<E>...holder){
        return ConfigHandler.getSetting(configName, this, holder);
    }

    /**
     * The global configurable object
     * for access to global configs
     */
    Configurable GLOBAL = new Configurable() {
        @Override
        public String getID() {
            return "GLOBAL-id";
        }
        @Override
        public ConfigLevel getConfigLevel() {
            return ConfigLevel.GLOBAL;
        }
    };

    /**
     * The map containing guild user configurables
     */
    HashMap<Guild, HashMap<User, Configurable>> GUILD_USERS = new HashMap<>();

    /**
     * The getter for a object that represents a guild user
     * @param guild the guild for a guild user object
     * @param user the user for the guild object
     * @return the guild user object for the guild and user
     */
    static Configurable getGuildUser(Guild guild, User user){
        return GUILD_USERS.computeIfAbsent(guild, g -> {
            HashMap<User, Configurable> map = new HashMap<>();
            GUILD_USERS.put(g, map);
            return map;
        }).computeIfAbsent(user, u -> new Configurable() {
            @Override
            public String getID() {
                return guild.getID() + "-id-" + user.getID();
            }
            @Override
            public ConfigLevel getConfigLevel() {
                return ConfigLevel.GUILD_USER;
            }
            @Override
            public boolean equals(Object o){
                return Configurable.class.isInstance(o) && this.getID().equals(((Configurable) o).getID());
            }
        });
    }
}
