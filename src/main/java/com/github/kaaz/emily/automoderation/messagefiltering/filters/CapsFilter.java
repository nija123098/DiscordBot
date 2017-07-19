package com.github.kaaz.emily.automoderation.messagefiltering.filters;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageFilter;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringException;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringType;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.stream.Stream;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class CapsFilter implements MessageFilter {
    private static final float ALLOWED = .40F;
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        String content = FormatHelper.filtering(event.getMessage().getContent(), character -> !Character.isWhitespace(character));
        if (Stream.of(content.split("")).map(s -> s.indexOf(0)).filter(Character::isUpperCase).count() / (float) content.length() >= ALLOWED) throw new MessageMonitoringException("excessive use of capitals");
    }
    @Override
    public MessageMonitoringType getType() {
        return MessageMonitoringType.EXCESSIVE_CAPITALS;
    }
}
