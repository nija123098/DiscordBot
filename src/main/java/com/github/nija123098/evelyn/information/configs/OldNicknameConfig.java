package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordNicknameChange;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class OldNicknameConfig extends AbstractConfig<Set<String>, GuildUser> {
    public OldNicknameConfig() {
        super("old_nickname_config", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "A list of old nicknames used by a user");
    }
    @EventListener
    public void handle(DiscordNicknameChange event) {
        if (event.getNewNickname() != null) this.alterSetting(GuildUser.getGuildUser(event.getGuild(), event.getUser()), strings -> strings.add(event.getNewNickname()));
        if (event.getOldNickname() != null) this.alterSetting(GuildUser.getGuildUser(event.getGuild(), event.getUser()), strings -> strings.add(event.getOldNickname()));
    }
}
