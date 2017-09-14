package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

public class TimeFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public TimeFavorFactorConfig() {
        super("time_favor_factor", BotRole.GUILD_TRUSTEE, .007F, "The time per 15min a user has been in the server without leaving");
    }
}
