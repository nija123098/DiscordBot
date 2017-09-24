package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.discordobjects.exception.ErrorWrapper;
import com.github.nija123098.evelyn.util.GetterUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class VoiceChannel extends Channel {
    static VoiceChannel getVoiceChannel(String id){// I have no clue how this hasn't thrown any NumberFormatExceptions so far
        return getVoiceChannel((IVoiceChannel) GetterUtil.getAny(DiscordClient.clients(), f -> f.getVoiceChannelByID(Long.parseLong(id))));
    }
    public static VoiceChannel getVoiceChannel(IVoiceChannel channel){
        return (VoiceChannel) Channel.getChannel(channel);
    }
    static List<VoiceChannel> getVoiceChannels(List<IVoiceChannel> iVoiceChannel){
        List<VoiceChannel> users = new ArrayList<>(iVoiceChannel.size());
        iVoiceChannel.forEach(iUser -> users.add(getVoiceChannel(iUser)));
        return users;
    }
    VoiceChannel(IVoiceChannel channel) {
        super(channel);
    }
    public IVoiceChannel channel(){
        return (IVoiceChannel) this.reference.get();
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
