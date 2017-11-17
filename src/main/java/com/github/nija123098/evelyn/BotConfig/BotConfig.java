package com.github.nija123098.evelyn.BotConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.github.nija123098.evelyn.util.PlatformDetector.PathEnding;

/**
 * @author Celestialdeath99
 */

public class BotConfig {

    private static Properties config;

    static {
        try {
            String FILEPATH = System.getProperty("user.dir");
            config = new Properties();
            config.load(new FileInputStream(FILEPATH + PathEnding() + "Bot-Config.cfg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key) {
        return config.getProperty(key);
    }


    public static long IMAGE_LOAD_CHANNEL = 0L;

    //Bot Settings
    public static final String BOT_TOKEN = getPropertyValue("Bot-Token");
    public static final boolean TESTING_MODE = Boolean.parseBoolean(getPropertyValue("Test-Mode-Enabled"));
    public static final int TOTAL_EVELYNS = Integer.parseInt(getPropertyValue("Total-Evelyn-Shards"));
    public static final int EVELYN_NUMBER = Integer.parseInt(getPropertyValue("Evelyn-Shard-Number"));
    public static final String SUPPORT_SERVER = getPropertyValue("Support-Server-ID");
    public static final boolean GHOST_MODE = Boolean.parseBoolean(getPropertyValue("Ghost-Mode-Enabled"));
    public static final int MESSAGE_FILTERING_SERVER_SIZE = Integer.parseInt(getPropertyValue("Message-Filtering-Server-Size"));
    public static final boolean TYPING_ENABLED = Boolean.parseBoolean(getPropertyValue("Typing-Enabled"));
    public static final boolean VOICE_COMMANDS_ENABLED = Boolean.parseBoolean(getPropertyValue("Voice-Commands-Enabled"));
    public static final String CONTRIBUTOR_SIGN_ROLE = getPropertyValue("Contributor-Sign-Role");
    public static final String SUPPORTER_SIGN_ROLE = getPropertyValue("Supporter-Sign-Role");

    //Database Settings
    public static final String DB_HOST = getPropertyValue("Database-IP");
    public static final String DB_PORT = getPropertyValue("Database-Port");
    public static final String DB_USER = getPropertyValue("Database-Username");
    public static final String DB_PASS = getPropertyValue("Database-Password");
    public static final String DB_NAME = getPropertyValue("Database-Name");

    //Service Auth Keys
    public static final String BITLY_TOKEN = getPropertyValue("Bitly-Token");
    public static final String CAT_API_TOKEN = getPropertyValue("Cat-API-Token");
    public static final String GIPHY_TOKEN = getPropertyValue("Giphy-API-Token");
    public static final String GOOGLE_API_KEY = getPropertyValue("Google-API-Key");
    public static final String RIOT_GAMES_TOKEN = getPropertyValue("Riot-Games-Token");
    public static final String TWITCH_ID = getPropertyValue("Twitch-ID");
    public static final String TWITTER_KEY = getPropertyValue("Twitter-Key");
    public static final String TWITTER_SECRET = getPropertyValue("Twitter-Secret-Key");

    //Audio Settings
    public static final String AUDIO_FILE_TYPES = getPropertyValue("Audio-File-Types");
    public static final String AUDIO_FORMAT = getPropertyValue("Audio-Format");
    public static final int MUSIC_DOWNLOAD_THREAD_COUNT = Integer.parseInt(getPropertyValue("Music-Downloader-Threads"));
    public static final long TRACK_EXPIRATION_TIME = Long.parseLong(getPropertyValue("Track-Expiration-Time"));
    public static final int REQUIRED_PLAYS_TO_DOWNLOAD  = Integer.parseInt(getPropertyValue("Required-Plays-To-Download"));

    //Folder Paths
    public static final String AUDIO_PATH = getPropertyValue("Audio-Path");
    public static final String BADGE_PATH = getPropertyValue("Badges-Path");
    public static final String LIBRARIES_PATH = getPropertyValue("Libraries-Folder");
    public static final String LOGS_PATH = getPropertyValue("Logs-Path");
    public static final String NEURAL_NET_FOLDER_PATH = getPropertyValue("Neural-Net-Path");
    public static final String TEMP_PATH = getPropertyValue("Temp-Path");
    public static final String REQUIRED_FILES_PATH = getPropertyValue("Required-Files-Path");

    //Required Files
    public static final String EMOTICONS_NAME = REQUIRED_FILES_PATH + getPropertyValue("Emoticons");
    public static final String FAKE_DANGER_NAME = REQUIRED_FILES_PATH + getPropertyValue("Fake-Danger");
    public static final String FINAL_MAP_NAME = REQUIRED_FILES_PATH + getPropertyValue("Final-Map");
    public static final String GUIDE_NAME = REQUIRED_FILES_PATH + getPropertyValue("Guide");
    public static final String LANGUAGE_FILTERING_NAME = REQUIRED_FILES_PATH + getPropertyValue("Language-Filtering");
    public static final String SUPPORTED_LANGS_NAME = REQUIRED_FILES_PATH + getPropertyValue("Supported-Languages");
    public static final String STATS_OVER_TIME_NAME = REQUIRED_FILES_PATH + getPropertyValue("Time-Stats");
    public static final String VERIFIED_GAMES_NAME = REQUIRED_FILES_PATH + getPropertyValue("Verified-Games-List");

    //Libraries paths
    public static final String FFM_PEG_PATH = getPropertyValue(LIBRARIES_PATH + "ffm_peg.exe");
    public static final String YT_DL_PATH = getPropertyValue("youtube-dl");
}
