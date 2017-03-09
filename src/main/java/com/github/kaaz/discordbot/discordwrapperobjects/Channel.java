package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import sx.blah.discord.handle.obj.IChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Channel implements Configurable {
    private static final Map<String, Channel> MAP = new ConcurrentHashMap<>();
    public static Channel getChannel(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
    }
    static Channel getChannel(IChannel channel){
        return MAP.computeIfAbsent(channel.getID(), s -> new Channel(channel));
    }
    static List<Channel> getChannels(List<IChannel> iChannels){
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    public synchronized void update(IChannel channel){// hash is based on id, so no old channel is necessary
        MAP.get(channel.getID()).reference.set(channel);
    }
    private final AtomicReference<IChannel> reference;
    private Channel(IChannel channel) {
        this.reference = new AtomicReference<>(channel);
    }

    @Override
    public String getID() {
        return null;
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.CHANNEL;
    }
}
