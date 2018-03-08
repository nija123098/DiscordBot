package com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig;

public interface CacheSettings {
    Integer categorySize();
    Integer guildSize();
    Integer channelSize();
    Integer userSize();
    Integer roleSize();
    Integer reactionSize();
    Integer messageSize();
    Integer presenceSize();
    Integer reactionBehaviorSize();
    Integer userBotRoleSize();
}
