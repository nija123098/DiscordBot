package com.github.nija123098.evelyn.perms.configs.standard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildBotRoleConfig extends AbstractConfig<Set<BotRole>, GuildUser> {
    public GuildBotRoleConfig() {
        super("guild_flag_ranks", "", ConfigCategory.GUILD_HIERARCHY, new HashSet<>(0), "Flags available to guild specific bot roles");
    }
}
