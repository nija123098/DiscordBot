package com.github.kaaz.emily.programconfig;

import com.wezinkhof.configuration.ConfigurationOption;

import java.io.File;

/**
 * Made by nija123098 on 3/13/2017.
 */
public class BotConfig {
    @ConfigurationOption
    public static String BOT_TOKEN = "Emily's Token";
    @ConfigurationOption
    public static String BOT_PATH = BotConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    @ConfigurationOption
    public static String CONTAINER_PATH = new File(BOT_PATH).getParent();
    @ConfigurationOption
    public static String AUDIO_PATH = "musicfiles";
    @ConfigurationOption
    public static String TEMP_PATH = "tempfiles";
}
