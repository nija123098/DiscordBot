package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class ReputationFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public ReputationFavorFactorConfig() {
        super("reputation_favor_factor", BotRole.GUILD_TRUSTEE, 5f, "The factor by which favor is bestowed on a guild user for a reaction");
    }
}
