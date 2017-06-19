package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

/**
 * Made by nija123098 on 5/4/2017.
 */
public class DiscordVoiceLeave implements BotEvent {
    private VoiceChannel channel;
    private User user;
    private boolean unique;
    public DiscordVoiceLeave(UserVoiceChannelLeaveEvent event) {
        this.channel = VoiceChannel.getVoiceChannel(event.getVoiceChannel());
        this.user = User.getUser(event.getUser());
        this.unique = true;
    }
    public DiscordVoiceLeave(IVoiceChannel channel, IUser user) {
        this.channel = VoiceChannel.getVoiceChannel(channel);
        this.user = User.getUser(user);
        this.unique = false;
    }
    public User getUser(){
        return this.user;
    }
    public VoiceChannel getChannel(){
        return this.channel;
    }
    public Guild getGuild(){
        return this.channel.getGuild();
    }
    public boolean isUnique(){
        return this.unique;
    }
}
