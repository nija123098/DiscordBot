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
        super("user_join_time", BotRole.BOT_ADMIN, "The first time a user joins a guild", guildUser -> guildUser.getGuild().getJoinTimeForUser(guildUser.getUser()));
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
