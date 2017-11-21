package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.exception.InvalidEventException;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;

public class DiscordUserNameChangeEvent implements BotEvent {
    private final String newName;
    private final String oldName;
    private final User user;
    public DiscordUserNameChangeEvent(UserUpdateEvent event){
        if (event.getNewUser().getName().equals(event.getOldUser().getName())) throw new InvalidEventException();
        this.newName = event.getNewUser().getName();
        this.oldName = event.getOldUser().getName();
        this.user = User.getUser(event.getNewUser());
    }
    public String getNewName() {
        return this.newName;
    }
    public String getOldName() {
        return this.oldName;
    }
    public User getUser() {
        return this.user;
    }
}
