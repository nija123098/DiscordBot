package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PingFilter implements MessageFilter {
    private static final Integer MAX_PINGS_ALLOWED = 2;
    private static final Map<GuildUser, Integer> PING_COUNT_MAP = new HashMap<>();
    private static final ScheduledExecutorService COUNT_REDUCTION_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        if (!(event.getMessage().getMentions().stream().anyMatch(user -> !user.isBot()) || event.getMessage().mentionsEveryone() || event.getMessage().mentionsHere())) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getAuthor());
        int pings = PING_COUNT_MAP.compute(guildUser, (gu, integer) -> integer == null ? 1 : ++integer);
        if (pings > MAX_PINGS_ALLOWED) throw new MessageMonitoringException("Too many mentions in too short a period");
        if (pings == 1) {
            final AtomicReference<ScheduledFuture<?>> reference = new AtomicReference<>();
            reference.set(COUNT_REDUCTION_EXECUTOR.scheduleAtFixedRate(() -> {
                if (PING_COUNT_MAP.compute(guildUser, (gu, integer) -> --integer < 1 ? null : integer) == null) reference.get().cancel(false);
            }, 1, 1, TimeUnit.MINUTES));
        }
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.EXCESSIVE_PINGING;
    }
}
