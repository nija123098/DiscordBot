package com.github.kaaz.emily.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IPresence;
import sx.blah.discord.handle.obj.StatusType;

import java.util.Optional;

/**
 * Made by nija123098 on 3/13/2017.
 */
public class Presence {
    private IPresence iPresence;
    Presence(IPresence iPresence){
        this.iPresence = iPresence;
    }
    public Optional<String> getOptionalPlayingText() {
        return iPresence.getPlayingText();
    }

    public String getPlayingText(){
        return iPresence.getPlayingText().orElse(null);
    }

    public Optional<String> getOptionalStreamingUrl() {
        return iPresence.getStreamingUrl();
    }

    public String getStreamingUrl(){
        return iPresence.getStreamingUrl().orElse(null);
    }

    public Status getStatus() {
        return null;
    }

    static Status get(StatusType type) {
        return Status.values()[type.ordinal()];
    }

    public enum Status {
        ONLINE,
        IDLE,
        OFFLINE,
        STREAMING,
        DND,
        UNKNOWN,;
    }
}