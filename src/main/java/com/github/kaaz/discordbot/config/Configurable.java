package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.User;

import java.util.List;

/**
 * Made by nija123098 on 2/20/2017.
 */
public interface Configurable {
    String getID();
    ConfigLevel getConfigLevel();
    default void setConfig(String configName, String...value){
        ConfigHandler.set(this, configName, value);
    }
    default String getConfig(String configName){
        return ConfigHandler.get(this, configName);
    }
    default List<String> getMulitConfig(String configName){
        return ConfigHandler.getMulti(this, configName);
    }
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
