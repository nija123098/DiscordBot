package com.github.nija123098.evelyn.favor.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildUserReputationConfig extends AbstractConfig<Integer, GuildUser> {
    public GuildUserReputationConfig() {
        super("guild_user_reputation", "", ConfigCategory.STAT_TRACKING, 0, "Guild based reputation for a guild member");
    }
}
