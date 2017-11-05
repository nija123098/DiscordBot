package com.github.nija123098.evelyn.launcher;


import com.wezinkhof.configuration.ConfigurationOption;
import java.io.File;
import static com.github.nija123098.evelyn.launcher.DeterminePaths.PathEnding;


public class BotConfig {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";

    //Bot Settings
    private static final String BotSettings = "####################\n### Bot Settings ###\n####################";

    @ConfigurationOption
    public static String BOT_TOKEN = "Bot Token";

    @ConfigurationOption
    public static String CONTAINER_PATH = new File(BotConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + PathEnding();

    @ConfigurationOption
    public static Boolean TESTING_MODE = false;

    @ConfigurationOption
    public static Integer TOTAL_EVELYNS = 1;

    @ConfigurationOption
    public static Integer EVELYN_NUMBER = 0;

    @ConfigurationOption
    public static String SUPPORT_SERVER = "Support Server ID";

    @ConfigurationOption
    public static Boolean GHOST_MODE = false;

    @ConfigurationOption
    public static Integer MESSAGE_FILTERING_SERVER_SIZE = 150;

    @ConfigurationOption
    public static Boolean TYPING_ENABLED = false;

    @ConfigurationOption
    public static long IMAGE_LOAD_CHANNEL = 0L;

    @ConfigurationOption
    public static Boolean VOICE_COMMANDS_ENABLED = false;

    @ConfigurationOption
    public static String CONTRIBUTOR_SIGN_ROLE = "-1";

    @ConfigurationOption
    public static String SUPPORTER_SIGN_ROLE = "-1";


    //Database Settings
    private static final String DatabaseSettings = "\n########################\n### Database Settigs ###\n########################";

    @ConfigurationOption
    public static String DB_HOST = "database-IP-address";

    @ConfigurationOption
    public static String DB_PORT = "database-port";

    @ConfigurationOption
    public static String DB_USER = "database-username";

    @ConfigurationOption
    public static String DB_PASS = "database-user-password";

    @ConfigurationOption
    public static String DB_NAME = "database-name";


    //Service Auth Keys
    private static final String ServiceAuthKeys = "\n#########################\n### Service Auth Keys ###\n#########################";

    @ConfigurationOption
    public static String BITLY_TOKEN = "bitly-token";

    @ConfigurationOption
    public static String CAT_API_TOKEN = null;

    @ConfigurationOption
    public static String GIPHY_TOKEN = null;

    @ConfigurationOption
    public static String GOOGLE_API_KEY = "api-key";

    @ConfigurationOption
    public static final String RIOT_GAMES_TOKEN = "riot-token";

    @ConfigurationOption
    public static String TWITCH_ID = null;

    @ConfigurationOption
    public static String TWITTER_KEY = "twitter-key";

    @ConfigurationOption
    public static String TWITTER_SECRET = "twitter-secret";


    //Audio Settings
    private static final String AudioSettings = "\n######################\n### Audio Settings ###\n######################";

    @ConfigurationOption
    public static String AUDIO_FILE_TYPES = "opus, mp3";

    @ConfigurationOption
    public static String AUDIO_FORMAT = "mp3";

    @ConfigurationOption
    public static int MUSIC_DOWNLOAD_THREAD_COUNT = 1;

    @ConfigurationOption
    public static Integer REQUIRED_PLAYS_TO_DOWNLOAD = 50;

    @ConfigurationOption
    public static Long TRACK_EXPIRATION_TIME = 432000000L;


    //Folder Paths
    private static final String FolderPaths = "\n####################\n### Folder Paths ###\n####################";

    @ConfigurationOption
    public static String AUDIO_PATH = CONTAINER_PATH + "musicFiles" + PathEnding();

    @ConfigurationOption
    public static String BADGE_PATH = CONTAINER_PATH + "badges" + PathEnding();

    @ConfigurationOption
    public static String LOGS_PATH = CONTAINER_PATH + "logs" + PathEnding();

    @ConfigurationOption
    public static String NEURAL_NET_FOLDER_PATH = CONTAINER_PATH + "neuralNets" + PathEnding();

    @ConfigurationOption
    public static String REQUIRED_TXTS_FOLDER_PATH = CONTAINER_PATH + "txts" + PathEnding();

    @ConfigurationOption
    public static String TEMP_PATH = CONTAINER_PATH + "tempFiles" + PathEnding();


    //File Names
    private static final String FileNames = "\n##################\n### File Names ###\n##################";

    @ConfigurationOption
    public static String FAKE_DANGER_NAME = REQUIRED_TXTS_FOLDER_PATH + "FakeDangerContents.txt";

    @ConfigurationOption
    public static String LANGUAGE_FILTERING_NAME = REQUIRED_TXTS_FOLDER_PATH + "LanguageFiltering.txt";

    @ConfigurationOption
    public static String STATS_OVER_TIME_NAME = REQUIRED_TXTS_FOLDER_PATH + "TimeStats.txt";

    @ConfigurationOption
    public static String VERIFIED_GAMES_NAME = REQUIRED_TXTS_FOLDER_PATH + "VerifiedGames.txt";


    //Libraries Paths
    private static final String LibrariesPaths = "\n#######################\n### Libraries Paths ###\n#######################";

    @ConfigurationOption
    public static String FFM_PEG_PATH = CONTAINER_PATH + "ffmpeg.exe";

    @ConfigurationOption
    public static String YT_DL_PATH = CONTAINER_PATH + "youtube-dl.exe";
}