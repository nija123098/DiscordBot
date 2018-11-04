package com.github.nija123098.evelyn.discordobjects.wrappers.event.events;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import sx.blah.discord.handle.impl.events.guild.GuildEmojisUpdateEvent;
import sx.blah.discord.handle.obj.IEmoji;

import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DiscordEmojiUpdate implements BotEvent {
    private GuildEmojisUpdateEvent event;
    public DiscordEmojiUpdate(GuildEmojisUpdateEvent event) {
        this.event = event;
    }
    public List<IEmoji> getOldEmoji() {
        return this.event.getOldEmojis();
    }
    public List<IEmoji> getNewEmoji() {
        return this.event.getNewEmojis();
    }
    public Guild getGuild() {
        return Guild.getGuild(this.event.getGuild());
    }
}
