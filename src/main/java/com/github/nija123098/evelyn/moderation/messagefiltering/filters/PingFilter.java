package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.util.CacheHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PingFilter implements MessageFilter {
    private static final CacheHelper.ContainmentCache<GuildUser> PINGERS = new CacheHelper.ContainmentCache<>(120_000);
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        if (!(event.getMessage().getMentions().stream().filter(user -> !user.isBot()).count() != 0 || event.getMessage().mentionsEveryone() || event.getMessage().mentionsHere())) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getAuthor());
        if (PINGERS.contains(guildUser)) {
            PINGERS.add(guildUser);
            throw new MessageMonitoringException("Too many mentions in too short a period");
        }
        PINGERS.add(guildUser);
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.EXCESSIVE_PINGING;
    }
}
