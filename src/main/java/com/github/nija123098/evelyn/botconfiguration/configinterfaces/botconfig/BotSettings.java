package com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public interface BotSettings {
    String botToken();
    Boolean testModeEnabled();
    Integer numberOfShards();
    Integer evelynShardNumber();
    String supportServerId();
    Boolean ghostModeEnabled();
    Integer messageFilteringServerSize();
    Boolean typingEnabled();
    Boolean voiceCommandsEnabled();
    String contributorSignRole();
    String supporterSignRole();
    String loggingChannel();
    String guildLogChannel();
    String userAgent();
    String botFolder();
    String startCommand();
}
