package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.moderation.messagefiltering.URLScanning;

public class InviteFilter implements MessageFilter {
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        URLScanning.getURLs(event.getMessage()).forEach(url -> {
            if (url.getHost().equals("discord.gg")) throw new MessageMonitoringException("Invites are not allowed here", true);
        });
    }

    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.INVITE;
    }
}
