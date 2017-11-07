package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserSpeakingEvent;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class DiscordSpeakingEvent implements BotEvent {
    private final UserSpeakingEvent event;
    public DiscordSpeakingEvent(UserSpeakingEvent event){
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
    public VoiceChannel getChannel(){
        return VoiceChannel.getVoiceChannel(this.event.getVoiceChannel());
    }
    public User getUser(){
        return User.getUser(this.event.getUser());
    }
    public boolean isSpeaking(){
        return this.event.isSpeaking();
    }
}
