package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * An assistant for making the bot display
 * the typing status during command processing.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class ProcessingHandler {
    private static final Map<Channel, Integer> PROCESSING_MAP = new ConcurrentHashMap<>();
    public static synchronized void startProcess(Channel channel){
        if (!ConfigProvider.BOT_SETTINGS.typingEnabled()|| channel == null) return;
        if (PROCESSING_MAP.compute(channel, (c, integer) -> integer == null ? 1 : ++integer) == 1) channel.setTypingStatus(true);
    }
    public static synchronized void endProcess(Channel channel){
        if (!ConfigProvider.BOT_SETTINGS.typingEnabled() || channel == null) return;
        if (PROCESSING_MAP.getOrDefault(channel, 0) == 1){
            PROCESSING_MAP.remove(channel);
            channel.setTypingStatus(false);
        } else PROCESSING_MAP.compute(channel, (c, integer) -> integer == null ? null : --integer);
    }
    public static void swapProcess(Channel origin, Channel destination){
        if (!ConfigProvider.BOT_SETTINGS.typingEnabled()) return;
        if (origin.equals(destination)) return;
        startProcess(destination);
        endProcess(origin);
    }
}
