package com.github.kaaz.emily.automoderation.messagefiltering.filters;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageFilter;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringException;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringType;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.service.AbstractService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 8/4/2017.
 */
public class SpamFilter extends AbstractService implements MessageFilter {
    private static final Map<User, Integer> MAP = new ConcurrentHashMap<>();
    public SpamFilter() {
        super(4000);
    }
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        if (MAP.compute(event.getAuthor(), (user, integer) -> integer == null ? 1 : ++integer) > 3) throw new MessageMonitoringException("Too much spam");
    }
    @Override
    public void run() {
        MAP.forEach((user, integer) -> {
            if (--integer < 1) MAP.remove(user);
            else MAP.put(user, integer);
        });
    }
    @Override
    public MessageMonitoringType getType() {
        return MessageMonitoringType.SPAM;
    }
}
