package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.util.FormatHelper;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a Discord4j {@link IVoiceChannel} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class VoiceChannel extends Channel {
    public static VoiceChannel getVoiceChannel(String id) {// I have no clue how this hasn't thrown any NumberFormatExceptions so far
        return (VoiceChannel) Channel.getChannel(id, true);
    }
    public static VoiceChannel getVoiceChannel(IVoiceChannel channel) {
        return (VoiceChannel) Channel.getChannel(channel);
    }
    static List<VoiceChannel> getVoiceChannels(List<IVoiceChannel> iVoiceChannel) {
        List<VoiceChannel> users = new ArrayList<>(iVoiceChannel.size());
        iVoiceChannel.forEach(iUser -> users.add(getVoiceChannel(iUser)));
        return users;
    }
    VoiceChannel(IVoiceChannel channel) {
        super(channel);
    }
    public IVoiceChannel channel() {
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
        ExceptionWrapper.wrap(() -> channel().edit(name, position, bitrate, userLimit));
    }

    public void changeBitrate(int bitrate) {
        ExceptionWrapper.wrap(() -> channel().changeBitrate(bitrate));
    }

    public void changeUserLimit(int limit) {
        ExceptionWrapper.wrap(() -> channel().changeUserLimit(limit));
    }

    public void join() {
        ExceptionWrapper.wrap(() -> channel().join());
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
