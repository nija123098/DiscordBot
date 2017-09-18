package com.github.nija123098.evelyn.favor.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class GuildUserReputationConfig extends AbstractConfig<Integer, GuildUser> {
    public GuildUserReputationConfig() {
        super("guild_user_reputation", ConfigCategory.STAT_TRACKING, 0, "Guild based reputation for a guild member");
    }
    public boolean checkDefault(){
        return false;
    }
}
