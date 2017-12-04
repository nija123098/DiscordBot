package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;

/**
 * @author nija123098
 * @since 1.0.0
 */
public interface MessageFilter {
    void checkFilter(DiscordMessageReceived event);
    MessageMonitoringLevel getType();
}
