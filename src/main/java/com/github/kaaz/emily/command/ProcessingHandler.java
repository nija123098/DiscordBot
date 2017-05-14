package com.github.kaaz.emily.command;

import com.github.kaaz.emily.discordobjects.wrappers.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ProcessingHandler {
    private static final Map<Channel, Integer> PROCESSING_MAP = new ConcurrentHashMap<>();
    public static synchronized void startProcess(Channel channel){
        if (PROCESSING_MAP.compute(channel, (c, integer) -> integer == null ? 1 : ++integer) == 1) channel.setTypingStatus(true);
    }
    public static synchronized void endProcess(Channel channel){
        if (PROCESSING_MAP.get(channel) == 1){
            PROCESSING_MAP.remove(channel);
            channel.setTypingStatus(false);
        } else PROCESSING_MAP.compute(channel, (c, integer) -> integer == null ? null : --integer);
    }
    public static void swapProcess(Channel origin, Channel destination){
        if (origin.equals(destination)) return;
        startProcess(destination);
        endProcess(origin);
    }
}
