package com.github.nija123098.evelyn.botconfiguration;

import com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig.*;
import com.github.nija123098.evelyn.botconfiguration.configinterfaces.urls.URLs;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */

public class ConfigProvider {

    //Settings from botconfig.yaml
    public static final BotSettings BOT_SETTINGS = ConfigLoader.configurationProvider().bind("BotSettings", BotSettings.class);
    public static final UpdateScripts UPDATE_SCRIPTS = ConfigLoader.configurationProvider().bind("UpdateScripts", UpdateScripts.class);
    public static final DatabaseSettings DATABASE_SETTINGS = ConfigLoader.configurationProvider().bind("DatabaseSettings", DatabaseSettings.class);
    public static final AuthKeys AUTH_KEYS = ConfigLoader.configurationProvider().bind("AuthKeys", AuthKeys.class);
    public static final AudioSettings AUDIO_SETTINGS = ConfigLoader.configurationProvider().bind("AudioSettings", AudioSettings.class);
    public static final FolderSettings FOLDER_SETTINGS = ConfigLoader.configurationProvider().bind("FolderSettings", FolderSettings.class);
    public static final ResourceFiles RESOURCE_FILES = ConfigLoader.configurationProvider().bind("ResourceFiles", ResourceFiles.class);
    public static final LibrariesFiles LIBRARIES_FILES = ConfigLoader.configurationProvider().bind("LibrariesFiles", LibrariesFiles.class);

    //Settings from urls.yaml
    public static final URLs URLS = ConfigLoader.configurationProvider().bind("urls", URLs.class);
}
