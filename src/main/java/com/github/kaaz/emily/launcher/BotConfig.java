package com.github.kaaz.emily.launcher;

import com.wezinkhof.configuration.ConfigurationOption;

import java.io.File;

/**
 * Made by nija123098 on 3/13/2017.
 */
public class BotConfig {
    @ConfigurationOption
    public static String BOT_TOKEN = "Emily's Token";
    @ConfigurationOption
    public static String CONTAINER_PATH = new File(BotConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "\\";
    @ConfigurationOption
    public static String AUDIO_PATH = CONTAINER_PATH + "musicfiles\\";
    @ConfigurationOption
    public static String TEMP_PATH = CONTAINER_PATH + "tempfiles\\";
    @ConfigurationOption
    public static String CONTRIBUTOR_SIGN_ROLE = "274957607855325184";
    @ConfigurationOption
    public static String GIPHY_TOKEN = null;
    @ConfigurationOption
    public static long IMAGE_LOAD_CHANNEL = 321103742743085057L;
    @ConfigurationOption
    public static String GOOGLE_API_KEY = null;
    @ConfigurationOption
    public static String YT_DL_PATH = CONTAINER_PATH + "youtube-dl.exe";
    @ConfigurationOption
    public static int MUSIC_DOWNLOAD_THREAD_COUNT = 1;
    @ConfigurationOption
    public static String AUDIO_FORMAT = "vorbis";
    @ConfigurationOption
    public static String CAT_API_TOKEN = null;
    @ConfigurationOption
    public static final String RIOT_GAMES_TOKEN = null;
    @ConfigurationOption
    public static String DB_HOST = "test";
    @ConfigurationOption
    public static String DB_USER = "emily";
    @ConfigurationOption
    public static String DB_PASS = "insert password here";
    @ConfigurationOption
    public static String DB_NAME = "emilybot";
}
