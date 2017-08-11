package com.github.kaaz.emily.favor.configs.derivation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserBanned;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserPardoned;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class BannedLocationConfig extends AbstractConfig<Set<Guild>, User> {
    public BannedLocationConfig() {
        super("banned_count", BotRole.SYSTEM, new HashSet<>(1, 1), "The number of bans a user has earned");
    }
    @EventListener
    public void handle(DiscordUserBanned event){
        this.alterSetting(event.getUser(), guilds -> guilds.remove(event.getGuild()));
    }
    @EventListener
    public void handle(DiscordUserPardoned event){
        this.alterSetting(event.getUser(), guilds -> guilds.remove(event.getGuild()));
    }
}
