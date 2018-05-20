package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildUserJoinTimeConfig extends AbstractConfig<Long, GuildUser> {
    private static GuildUserJoinTimeConfig config;

    public GuildUserJoinTimeConfig() {
        super("user_join_time", "", ConfigCategory.STAT_TRACKING, GuildUser::getJoinTime, "The first time a user joins a guild");
        config = this;
    }

    public static long get(GuildUser guildUser) {
        Long aLong = config.getValue(guildUser);
        if (aLong == null) {
            aLong = guildUser.getJoinTime();
            config.setValue(guildUser, aLong);
        }
        return aLong;
    }

    @EventListener
    public void handle(DiscordUserJoin event) {
        if (event.getGuild() == null || event.getUser() == null) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getUser());
        if (guildUser == null) return;
        if (this.getValue(guildUser) == null) this.setValue(guildUser, event.getJoinTime());
    }
}