package com.github.kaaz.emily.config;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.HashMap;
import java.util.Map;

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
     * The global configurable object
     * for access to global configs
     */
    GlobalConfigurable GLOBAL = new GlobalConfigurable();

    /**
     * The class for the global configurable
     */
    class GlobalConfigurable implements Configurable {
        private GlobalConfigurable(){}
        @Override
        public String getID() {
            return "GLOBAL-id";
        }
        @Override
        public ConfigLevel getConfigLevel() {
            return ConfigLevel.GLOBAL;
        }
    }

    /**
     * The map containing guild user configurables
     */
    Map<String, GuildUser> GUILD_USERS = new HashMap<>();

    /**
     * The getter for a object that represents a guild user
     *
     * @param guild the guild for a guild user object
     * @param user the user for the guild object
     * @return the guild user object for the guild and user
     */
    static GuildUser getGuildUser(Guild guild, User user){
        return getGuildUser(guild.getID() + "-id-" + user.getID());
    }

    /**
     * The getter for a object that represents a guild user
     *
     * @param id the id of the guild user
     * @return the guild user object for the guild and user
     */
    static GuildUser getGuildUser(String id){
        return GUILD_USERS.computeIfAbsent(id, s -> new GuildUser(id));
    }

    /**
     * The configurable for users within a guild
     */
    class GuildUser implements Configurable {
        private String id;
        private GuildUser(String id) {
            this.id = id;
        }
        @Override
        public String getID() {
            return this.id;
        }
        @Override
        public ConfigLevel getConfigLevel() {
            return ConfigLevel.GUILD_USER;
        }
        @Override
        public boolean equals(Object o){
            return Configurable.class.isInstance(o) && this.getID().equals(((Configurable) o).getID());
        }
        public Guild getGuild(){
            return Guild.getGuild(this.id.split("-id-")[0]);
        }
        public User getUser(){
            return User.getUser(this.id.split("-id-")[1]);
        }
    }
}
