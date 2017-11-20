package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordNicknameChange;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class OldNicknameConfig extends AbstractConfig<Set<String>, GuildUser> {
    public OldNicknameConfig() {
        super("old_nickname_config", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "A list of old nicknames used by a user");
    }
    @EventListener
    public void handle(DiscordNicknameChange event){
        if (event.getNewUsername() != null) this.alterSetting(GuildUser.getGuildUser(event.getGuild(), event.getUser()), strings -> strings.add(event.getNewUsername()));
        if (event.getOldUsername() != null) this.alterSetting(GuildUser.getGuildUser(event.getGuild(), event.getUser()), strings -> strings.add(event.getOldUsername()));
    }
}
