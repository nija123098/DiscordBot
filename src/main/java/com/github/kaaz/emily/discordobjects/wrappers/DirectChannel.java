package com.github.kaaz.emily.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/11/2017.
 */
public class DirectChannel extends Channel {
    public static DirectChannel getDirectChannel(String id){
        return (DirectChannel) Channel.getChannel(id);
    }
    static DirectChannel getDirectChannel(IChannel channel){
        return (DirectChannel) Channel.getChannel(channel);
    }
    static List<Channel> getChannels(List<IChannel> iChannels){
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    DirectChannel(IPrivateChannel channel) {
        super(channel);
    }
    IPrivateChannel channel(){
        return (IPrivateChannel) super.channel();
    }
    public User getRecipient() {
        return User.getUser(channel().getRecipient());
    }
}
