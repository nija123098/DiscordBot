package com.github.kaaz.emily.automoderation.messagefiltering.filters;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageFilter;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringException;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringLevel;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.util.FormatHelper;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class CapsFilter implements MessageFilter {
    private static final float ALLOWED = .40F;
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        String content = FormatHelper.filtering(event.getMessage().getContent(), character -> !Character.isWhitespace(character));
        if (content.length() < 100) return;
        int capsCount = 0;
        boolean skip = true;
        for (char c : content.toCharArray()){
            if (skip) {
                skip = false;
                continue;
            }
            if (c == ' ') skip = true;
            if (Character.isUpperCase(c) && ++capsCount / (float) content.length() >= ALLOWED) throw new MessageMonitoringException("excessive use of capitals");
        }
    }
    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.EXCESSIVE_CAPITALS;
    }
}
