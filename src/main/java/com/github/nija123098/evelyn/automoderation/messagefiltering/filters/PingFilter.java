package com.github.nija123098.evelyn.automoderation.messagefiltering.filters;

import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;

import java.util.List;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class PingFilter implements MessageFilter {
    private static final List<GuildUser> PINGERS = new MemoryManagementService.ManagedList<>(120_000);
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        if (!(event.getMessage().getMentions().stream().filter(user -> !user.isBot()).count() != 0 || event.getMessage().mentionsEveryone() || event.getMessage().mentionsHere())) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getAuthor());
        if (PINGERS.contains(guildUser)) {
            PINGERS.add(guildUser);
            throw new MessageMonitoringException("too many mentions in too short a period");
        }
        PINGERS.add(guildUser);
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.EXCESSIVE_PINGING;
    }
}
