package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserBanned;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserPardoned;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BannedLocationConfig extends AbstractConfig<Set<Guild>, User> {
    public BannedLocationConfig() {
        super("banned_count", "", ConfigCategory.STAT_TRACKING, new HashSet<>(2, 1), "The location of bans a user has received");
    }
    @EventListener
    public void handle(DiscordUserBanned event) {
        this.alterSetting(event.getUser(), guilds -> guilds.add(event.getGuild()));
    }
    @EventListener
    public void handle(DiscordUserPardoned event) {
        this.alterSetting(event.getUser(), guilds -> guilds.remove(event.getGuild()));
    }
}
