package com.github.kaaz.discordbot.config;

/**
 * Made by nija123098 on 2/20/2017.
 */
public interface Configurable {
    String getID();
    ConfigLevel getConfigLevel();

    Configurable GLOBAL = new Configurable() {
        @Override
        public String getID() {
            return "GLOBAL_CONFIGURABLE";
        }
        @Override
        public ConfigLevel getConfigLevel() {
            return ConfigLevel.GLOBAL;
        }
    };
}
