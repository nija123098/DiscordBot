package com.github.nija123098.evelyn.botConfiguration;

import com.github.nija123098.evelyn.botConfiguration.configInterfaces.BotConfig.*;
import com.github.nija123098.evelyn.botConfiguration.configInterfaces.URLs.URLs;

/**
 * @Author: Celestialdeath99
 * Made on 11/28/2017
 */

public class ConfigProvider {

    //Settings from BotConfig.yaml
    public static final BotSettings botSettings = ConfigLoader.configurationProvider().bind("BotSettings", BotSettings.class);
    public static final UpdateScripts updateScripts = ConfigLoader.configurationProvider().bind("UpdateScripts", UpdateScripts.class);
    public static final DatabaseSettings databaseSettings = ConfigLoader.configurationProvider().bind("DatabaseSettings", DatabaseSettings.class);
    public static final AuthKeys authKeys = ConfigLoader.configurationProvider().bind("AuthKeys", AuthKeys.class);
    public static final AudioSettings audioSettings = ConfigLoader.configurationProvider().bind("AudioSettings", AudioSettings.class);
    public static final FolderSettings folderSettings = ConfigLoader.configurationProvider().bind("FolderSettings", FolderSettings.class);
    public static final ResourceFiles resourceFiles = ConfigLoader.configurationProvider().bind("ResourceFiles", ResourceFiles.class);
    public static final LibrariesFiles librariesFiles = ConfigLoader.configurationProvider().bind("LibrariesFiles", LibrariesFiles.class);

    //Settings from URLs.yaml
    public static final URLs urls = ConfigLoader.configurationProvider().bind("URLs", URLs.class);
}
