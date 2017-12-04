package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageDelete;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageCountConfig extends AbstractConfig<Integer, GuildUser> {
    public MessageCountConfig() {
        super("message_count", "", ConfigCategory.STAT_TRACKING, 0, "The number of messages a user has made");
    }
    @EventListener
    public void handle(DiscordMessageReceived event){
        if (event.getMessage().getChannel().isPrivate()) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getAuthor());
        FavorChangeEvent.process(guildUser, () -> this.changeSetting(guildUser, integer -> ++integer));
    }
    @EventListener
    public void handle(DiscordMessageDelete event){
        if (event.getMessage() == null || event.getMessage().getChannel().isPrivate()) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getAuthor());
        FavorChangeEvent.process(guildUser, () -> this.changeSetting(guildUser, integer -> --integer));
    }
}
