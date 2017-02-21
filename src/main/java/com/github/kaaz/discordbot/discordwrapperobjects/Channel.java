package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Channel implements Configurable {
    @Override
    public String getID() {
        return null;
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.CHANNEL;
    }
}
