package com.github.kaaz.discordbot.discordobjects.wrappers;

import com.github.kaaz.discordbot.discordobjects.exception.ErrorWrapper;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class VoiceChannel extends Channel {
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
    public synchronized void update(IVoiceChannel guild){
        MAP.get(guild.getID()).reference.set(guild);
    }
    private final AtomicReference<IVoiceChannel> reference;
    private VoiceChannel(IVoiceChannel channel) {
        super(channel);
        this.reference = new AtomicReference<>(channel);
    }
    IVoiceChannel channel(){
        return this.reference.get();
    }
    // THE FOLLOWING ARE WRAPPER METHODS
    public int getUserLimit() {
        return channel().getUserLimit();
    }

    public int getBitrate() {
        return channel().getBitrate();
    }

    public void edit(String name, int position, int bitrate, int userLimit) {
        ErrorWrapper.wrap(() -> channel().edit(name, position, bitrate, userLimit));
    }

    public void changeBitrate(int bitrate) {
        ErrorWrapper.wrap(() -> channel().changeBitrate(bitrate));
    }

    public void changeUserLimit(int limit) {
        ErrorWrapper.wrap(() -> channel().changeUserLimit(limit));
    }

    public void join() {
        ErrorWrapper.wrap(() -> channel().join());
    }

    public void leave() {
        channel().leave();
    }

    public boolean isConnected() {
        return channel().isConnected();
    }

    public VoiceChannel copy() {
        return getVoiceChannel(channel().copy());
    }

    public List<User> getConnectedUsers() {
        return User.getUsers(channel().getConnectedUsers());
    }
}
