package com.github.nija123098.evelyn.perms.configs.standard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class GuildBotRoleConfig extends AbstractConfig<Set<BotRole>, GuildUser> {
    public GuildBotRoleConfig() {
        super("guild_flag_ranks", BotRole.GUILD_ADMIN, new HashSet<>(0), "Flags available to guild specific bot roles");
    }
}