package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.User;
import com.github.kaaz.discordbot.util.Holder;

/**
 * Made by nija123098 on 2/20/2017.
 */
public interface Configurable {
    String getID();
    ConfigLevel getConfigLevel();
    // these might be broken due to unchecked args
    default <E> void setSetting(Class<? extends AbstractConfig<E>> clazz, E o){
        ConfigHandler.setSetting(clazz, this, o);
    }
    default void setSetting(String configName, Object o){
        ConfigHandler.setSetting(configName, this, o);
    }
    @SuppressWarnings("unchecked")
    default  <E> E getSetting(Class<? extends AbstractConfig<E>> clazz, Holder<E>...holder){
        return ConfigHandler.getSetting(clazz, this, holder);
    }
    @SuppressWarnings("unchecked")
    default <E> E getSetting(String configName, Holder<E>...holder){
        return ConfigHandler.getSetting(configName, this, holder);
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
            @Override
            public boolean equals(Object o){
                return Configurable.class.isInstance(o) && this.getID().equals(((Configurable) o).getID());
            }
        };
    }
}
