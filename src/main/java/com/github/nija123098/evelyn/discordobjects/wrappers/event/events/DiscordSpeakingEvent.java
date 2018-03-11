package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserSpeakingEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordSpeakingEvent implements BotEvent {
    private final UserSpeakingEvent event;
    public DiscordSpeakingEvent(UserSpeakingEvent event) {
        this.event = event;
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
    public VoiceChannel getChannel() {
        return VoiceChannel.getVoiceChannel(this.event.getVoiceChannel());
    }
    public User getUser() {
        return User.getUser(this.event.getUser());
    }
    public boolean isSpeaking() {
        return this.event.isSpeaking();
    }
}
