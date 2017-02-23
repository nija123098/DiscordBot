package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.User;

/**
 * Made by nija123098 on 2/20/2017.
 */
public interface Configurable {
    String getID();
    ConfigLevel getConfigLevel();
    default void config(String configName, String...value){
        ConfigHandler.set(this, configName, value);
    }
    Configurable GLOBAL = new Configurable() {
        @Override
        public String getID() {
            return "GLOBAL-CONFIGURABLE";
        }
        @Override
        public ConfigLevel getConfigLevel() {
            return ConfigLevel.GLOBAL;
        }
    };
    static Configurable getGuildUser(Guild guild, User user){
        return new Configurable() {
            @Override
            public String getID() {
                return guild.getID() + "-id-" + user.getID();
            }
            @Override
            public ConfigLevel getConfigLevel() {
                return ConfigLevel.GUILD_USER;
            }
        };
    }
}
