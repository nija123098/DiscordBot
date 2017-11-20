package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserBanned;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserPardoned;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class BannedLocationConfig extends AbstractConfig<Set<Guild>, User> {
    public BannedLocationConfig() {
        super("banned_count", "", ConfigCategory.STAT_TRACKING, new HashSet<>(1, 1), "The number of bans a user has earned");
    }
    @EventListener
    public void handle(DiscordUserBanned event){
        FavorChangeEvent.process(event.getUser(), () -> this.alterSetting(event.getUser(), guilds -> guilds.remove(event.getGuild())));
    }
    @EventListener
    public void handle(DiscordUserPardoned event){
        FavorChangeEvent.process(event.getUser(), () -> this.alterSetting(event.getUser(), guilds -> guilds.remove(event.getGuild())));
    }
}
