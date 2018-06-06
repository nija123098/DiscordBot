package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;

import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class WhiteSpaceFilter implements MessageFilter {
    private static final float ALLOWED = .25F;
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        String content = event.getMessage().getContent();
        if (Stream.of(content.split("")).map(s -> s.indexOf(0)).filter(Character::isWhitespace).count() / (float) content.length() >= ALLOWED) throw new MessageMonitoringException("Too much white space", true);
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.WHITE_SPACE;
    }
}
