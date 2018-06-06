package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CapsFilter implements MessageFilter {
    private static final float ALLOWED = .40F;
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        String content = FormatHelper.filtering(event.getMessage().getContent(), character -> !Character.isWhitespace(character));
        if (content.length() < 100) return;
        int capsCount = 0;
        boolean skip = true;
        for (char c : content.toCharArray()) {
            if (skip) {
                skip = false;
                continue;
            }
            if (c == ' ') skip = true;
            else if (Character.isUpperCase(c) && ++capsCount / (float) content.length() >= ALLOWED) throw new MessageMonitoringException("Excessive use of capitals", true);
        }
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.EXCESSIVE_CAPITALS;
    }
}
