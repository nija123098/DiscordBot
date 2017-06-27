package com.github.kaaz.emily.discordobjects.wrappers.event.botevents;

import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;

/**
 * Made by nija123098 on 6/27/2017.
 */
public class SpeechRecognitionEvent implements BotEvent {
    private String string;
    private GuildAudioManager manager;
    private User user;
    public SpeechRecognitionEvent(String string, GuildAudioManager manager, User user) {
        this.string = string;
        this.manager = manager;
        this.user = user;
    }

    public String getSpeech() {
        return this.string;
    }

    public GuildAudioManager getManager() {
        return this.manager;
    }

    public User getUser() {
        return this.user;
    }
}
