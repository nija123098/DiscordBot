package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Presence;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.PresenceUpdateEvent;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class DiscordPresenceUpdate implements BotEvent {
    private PresenceUpdateEvent event;
    public DiscordPresenceUpdate(PresenceUpdateEvent event) {
        this.event = event;
    }
    public Presence getOldPresence(){
        return Presence.getPresence(this.event.getOldPresence());
    }
    public Presence getNewPresence(){
        return Presence.getPresence(this.event.getNewPresence());
    }
    public User getUser(){
        return User.getUser(this.event.getUser());
    }
}
