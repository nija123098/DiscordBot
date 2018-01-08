package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;

/**
 * Made by nija123098 on 7/19/2017.
 */
public interface MessageFilter {
    void checkFilter(DiscordMessageReceived event);
    MessageMonitoringLevel getType();
}
