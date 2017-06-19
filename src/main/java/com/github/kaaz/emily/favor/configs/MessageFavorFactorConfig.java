package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageDeleteEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.favor.FavorHandler;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class MessageFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public MessageFavorFactorConfig() {
        super("message_favor_factor", BotRole.GUILD_TRUSTEE, 1F, "The factor by which favor is bestowed on a guild user for a message");
    }
    @EventListener
    public void handle(DiscordMessageReceivedEvent event){
        if (event.getGuild() != null) FavorHandler.addFavorLevel(GuildUser.getGuildUser(event.getGuild(), event.getAuthor()), this.getValue(event.getGuild()));
    }
    @EventListener
    public void handle(DiscordMessageDeleteEvent event){
        if (event.getGuild() != null) FavorHandler.addFavorLevel(GuildUser.getGuildUser(event.getGuild(), event.getAuthor()), -this.getValue(event.getGuild()));
    }
}
