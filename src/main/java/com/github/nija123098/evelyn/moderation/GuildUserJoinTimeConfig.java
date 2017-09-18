package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class GuildUserJoinTimeConfig extends AbstractConfig<Long, GuildUser> {
    public GuildUserJoinTimeConfig() {
        super("user_join_time", ConfigCategory.STAT_TRACKING, guildUser -> guildUser.getGuild().getJoinTimeForUser(guildUser.getUser()), "The first time a user joins a guild");
        config = this;
    }
    private static GuildUserJoinTimeConfig config;
    public static long get(GuildUser guildUser){
        Long aLong = config.getValue(guildUser);
        if (aLong == null){
            aLong = guildUser.getGuild().getJoinTimeForUser(guildUser.getUser());
            config.setValue(guildUser, aLong);
        }
        return aLong;
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getUser());
        if (this.getValue(guildUser) != null) return;
        this.setValue(guildUser, event.getJoinTime());
    }
    public boolean checkDefault(){
        return false;
    }
}
