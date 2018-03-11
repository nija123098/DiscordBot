package com.github.nija123098.evelyn.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a Discord4J {@link IPrivateChannel} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class DirectChannel extends Channel {
    public static DirectChannel getDirectChannel(String id) {
        return (DirectChannel) getChannel(id);
    }
    static DirectChannel getDirectChannel(IChannel channel) {
        return (DirectChannel) getChannel(channel);
    }
    static List<Channel> getChannels(List<IChannel> iChannels) {
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    DirectChannel(IPrivateChannel channel) {
        super(channel);
    }
    public IPrivateChannel channel() {
        return (IPrivateChannel) super.channel();
    }
    public User getRecipient() {
        return User.getUser(channel().getRecipient());
    }
}
