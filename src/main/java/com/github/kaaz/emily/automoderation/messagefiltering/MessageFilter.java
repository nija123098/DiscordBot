package com.github.kaaz.emily.automoderation.messagefiltering;

import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;

/**
 * Made by nija123098 on 7/19/2017.
 */
public interface MessageFilter {
    void checkFilter(DiscordMessageReceived event);
    MessageMonitoringType getType();
}
