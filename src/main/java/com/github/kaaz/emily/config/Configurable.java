package com.github.kaaz.emily.config;

import com.github.kaaz.emily.config.configs.guilduser.GuildFlagRankConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.Holder;

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
public interface Configurable<T extends Configurable<T>> {
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
     * The global configurable object
     * for access to global configs
     */
    Configurable GLOBAL = new GlobalConfigurable();
    class GlobalConfigurable implements Configurable<GlobalConfigurable> {
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
    HashMap<Guild, HashMap<User, GuildUser>> GUILD_USERS = new HashMap<>();

    /**
     * The getter for a object that represents a guild user
     * @param guild the guild for a guild user object
     * @param user the user for the guild object
     * @return the guild user object for the guild and user
     */
    static GuildUser getGuildUser(Guild guild, User user){
        return GUILD_USERS.computeIfAbsent(guild, g -> {
            HashMap<User, GuildUser> map = new HashMap<>();
            GUILD_USERS.put(g, map);
            return map;
        }).computeIfAbsent(user, u -> new GuildUser(user, guild));
    }
    class GuildUser implements Configurable<GuildUser> {
        private User user;
        private Guild guild;
        private GuildUser(User user, Guild guild) {
            this.user = user;
            this.guild = guild;
        }
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
    }
}
