package com.github.kaaz.discordbot.discordwrapperobjects;

import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class VoiceChannel {
    private static final Map<String, VoiceChannel> MAP = new ConcurrentHashMap<>();
    public static VoiceChannel getVoiceChannel(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
    }
    static VoiceChannel getVoiceChannel(IVoiceChannel channel){
        return MAP.computeIfAbsent(channel.getID(), s -> new VoiceChannel(channel));
    }
    static List<VoiceChannel> getVoiceChannels(List<IVoiceChannel> iVoiceChannel){
        List<VoiceChannel> users = new ArrayList<>(iVoiceChannel.size());
        iVoiceChannel.forEach(iUser -> users.add(getVoiceChannel(iUser)));
        return users;
    }
    public synchronized void update(IVoiceChannel guild){// hash is based on id, so no old channel is necessary
        MAP.get(guild.getID()).reference.set(guild);
    }
    private final AtomicReference<IVoiceChannel> reference;
    private VoiceChannel(IVoiceChannel guild) {
        this.reference = new AtomicReference<>(guild);
    }
    IVoiceChannel channel(){
        return this.reference.get();
    }
}
