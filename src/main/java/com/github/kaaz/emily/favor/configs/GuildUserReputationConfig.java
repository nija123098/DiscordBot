package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class GuildUserReputationConfig extends AbstractConfig<Integer, GuildUser> {
    public GuildUserReputationConfig() {
        super("guild_user_reputation", BotRole.GUILD_TRUSTEE, 0, "Guild based reputation for a guild member");
    }
    public boolean checkDefault(){
        return false;
    }
}
