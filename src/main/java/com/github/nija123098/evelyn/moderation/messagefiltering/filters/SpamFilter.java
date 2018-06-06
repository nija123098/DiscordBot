package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.service.AbstractService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SpamFilter extends AbstractService implements MessageFilter {
    private static final Map<User, Integer> MAP = new ConcurrentHashMap<>();
    public SpamFilter() {
        super(4000);
    }
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        if (MAP.compute(event.getAuthor(), (user, integer) -> integer == null ? 1 : ++integer) > 3) throw new MessageMonitoringException("Too much spam", true);
    }
    @Override
    public void run() {
        MAP.forEach((user, integer) -> {
            if (--integer < 1) MAP.remove(user);
            else MAP.put(user, integer);
        });
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.SPAM;
    }
}
