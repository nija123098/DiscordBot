package com.github.nija123098.evelyn.discordobjects.wrappers.event.botevents;

import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;

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
