package com.github.nija123098.evelyn.automoderation.messagefiltering.filters;

import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;

import java.util.stream.Stream;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class WhiteSpaceFilter implements MessageFilter {
    private static final float ALLOWED = .25F;
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        String content = event.getMessage().getContent();
        if (Stream.of(content.split("")).map(s -> s.indexOf(0)).filter(Character::isWhitespace).count() / (float) content.length() >= ALLOWED) throw new MessageMonitoringException("too much white space");
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.WHITE_SPACE;
    }
}
