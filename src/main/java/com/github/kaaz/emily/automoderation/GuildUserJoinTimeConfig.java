package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class GuildUserJoinTimeConfig extends AbstractConfig<Long, GuildUser> {
    public GuildUserJoinTimeConfig() {
        super("user_join_time", BotRole.BOT_ADMIN, null, "The first time a user joins a guild");
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getUser());
        if (this.getValue(guildUser) != null) return;
        this.setValue(guildUser, event.getJoinTime());
    }
}
