package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.util.Log;
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordNicknameChange implements BotEvent {
    private NicknameChangedEvent event;
    public DiscordNicknameChange(NicknameChangedEvent event) {
        this.event = event;
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
    public User getUser() {
        return User.getUser(this.event.getUser());
    }
    public String getOldNickname() {
        return this.event.getOldNickname().orElse(User.getUser(this.event.getUser()).getName());
    }
    public String getNewNickname() {
        return this.event.getNewNickname().orElse(User.getUser(this.event.getUser()).getName());
    }
}
